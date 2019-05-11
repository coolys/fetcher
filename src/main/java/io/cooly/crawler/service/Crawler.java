/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.cooly.crawler.service;

/**
 *
 * @author hungnguyendang
 */
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.NamedRunnable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Fetches HTML from a requested URL, follows the links, and repeats.
 */
public final class Crawler {

    private final OkHttpClient client;
    private final Set<HttpUrl> fetchedUrls = Collections.synchronizedSet(new LinkedHashSet<>());
    private final LinkedBlockingQueue<HttpUrl> queue = new LinkedBlockingQueue<>();
    private final ConcurrentHashMap<String, AtomicInteger> hostnames = new ConcurrentHashMap<>();
    //private final String currentUrl;

    public Crawler(OkHttpClient client, String url) {
        this.client = client;
        //this.currentUrl = url;
        this.queue.add(HttpUrl.get(url));

    }

    public void parallelDrainQueue(int threadCount) {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executor.execute(new NamedRunnable("Crawler %s", i) {
                @Override
                protected void execute() {
                    try {
                        drainQueue();
                    } catch (Exception e) {
                    }
                }
            });
        }
        executor.shutdown();
    }

    public void drainQueue() throws Exception {
        for (HttpUrl url; (url = queue.take()) != null;) {
            if (!fetchedUrls.add(url)) {
                continue;
            }

            Thread currentThread = Thread.currentThread();
            String originalName = currentThread.getName();
            currentThread.setName("Crawler " + url.toString());
            try {
                fetch(url);
            } catch (IOException e) {
                System.out.printf("XXX: %s %s%n", url, e);
            } finally {
                currentThread.setName(originalName);
            }
        }
    }

    public void fetch(HttpUrl url) throws IOException {
        // Skip hosts that we've visited many times.
        AtomicInteger hostnameCount = new AtomicInteger();
        AtomicInteger previous = hostnames.putIfAbsent(url.host(), hostnameCount);
        if (previous != null) {
            hostnameCount = previous;
        }
        if (hostnameCount.incrementAndGet() > 100) {
            return;
        }

        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String responseSource;
            responseSource = response.networkResponse() != null ? (response.networkResponse().code()
                    + "(network: "
                    + " over "
                    + response.protocol()
                    + ")") : "(cache)";
            int responseCode = response.code();

            System.out.printf("%03d: %s %s %s%n", responseCode, url.host(), url, responseSource);

            String contentType = response.header("Content-Type");
            if (responseCode != 200 || contentType == null) {
                return;
            }

            MediaType mediaType = MediaType.parse(contentType);
            if (mediaType == null || !mediaType.subtype().equalsIgnoreCase("html")) {
                return;
            }

            // should be in
            Document document = Jsoup.parse(response.body().string(), url.toString());
            document.select("a[href]").stream().map((element) -> element.attr("href")).map((href) -> response.request().url().resolve(href)).filter((link) -> !(link == null)).forEachOrdered((link) -> {
                // URL is either invalid or its scheme isn't http/https.
                // send back to fetcher
                queue.add(link.newBuilder().fragment(null).build());
            });
        }
    }

//  public void addUrlToQueue(String url) {
//     this.queue.add(HttpUrl.get(url));
//  } 
//  public static void main(String[] args) throws IOException, Exception {
//    int threadCount = 20;
//    long cacheByteCount = 1024L * 1024L * 100L;
//
//    //Cache cache = new Cache(new File(args[0]), cacheByteCount);
//    OkHttpClient client = new OkHttpClient.Builder()
//        //.cache(cache)
//        .build();
//    Crawler crawler = new Crawler(client);
//    crawler.queue.add(HttpUrl.get("https://vnexpress.net/"));        
//    crawler.drainQueue();
//    crawler.parallelDrainQueue(threadCount);
//  }
}
