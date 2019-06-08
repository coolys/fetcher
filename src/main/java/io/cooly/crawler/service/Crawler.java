/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.cooly.crawler.service;

/**
 * @author hungnguyendang
 */

import io.cooly.crawler.client.FetcherServiceClient;
import io.cooly.crawler.domain.WebUrl;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fetches HTML from a requested URL, follows the links, and repeats.
 */
public final class Crawler {

    private final Logger log = LoggerFactory.getLogger(Crawler.class);
    private final Set<HttpUrl> fetchedUrls = Collections.synchronizedSet(new LinkedHashSet<>());
    private final LinkedBlockingQueue<HttpUrl> queue = new LinkedBlockingQueue<>();
    private final ConcurrentHashMap<String, AtomicInteger> hostnames = new ConcurrentHashMap<>();
    private final HttpUrl url;
    private final FetcherServiceClient fetchEngine;
    private final QueueService queueService;
    private final OkHttpClient client;

    public Crawler(WebUrl webUrl, FetcherServiceClient fetchEngine, QueueService queueService) {
        HttpUrl currentUrl = HttpUrl.get(webUrl.getUrl());
        this.queue.add(currentUrl);
        this.url = currentUrl;
        this.fetchEngine = fetchEngine;
        this.queueService = queueService;

        OkHttpClient httpClient = new OkHttpClient.Builder()
            .build();
        this.client = httpClient;
        this.crawl(currentUrl, webUrl);

    }


    public void crawl(HttpUrl queueUrl, WebUrl webUrl) {
        try {
            log.info("crawl fetch link: {}", queueUrl.url().toString());

            // Skip hosts that we've visited many times.
            AtomicInteger hostnameCount = new AtomicInteger();
            Set<HttpUrl> nextLinks = new HashSet<>();
            AtomicInteger previous = hostnames.putIfAbsent(queueUrl.host(), hostnameCount);
            if (previous != null) {
                hostnameCount = previous;
            }
            if (hostnameCount.incrementAndGet() > 100) {
                return;
            }

            Request request = new Request.Builder()
                .url(queueUrl)
                .build();
            try (Response response = client.newCall(request).execute()) {
                String responseSource;
                responseSource = response.networkResponse() != null ? (response.networkResponse().code()
                    + "(network: "
                    + " over "
                    + response.protocol()
                    + ")") : "(cache)";
                int responseCode = response.code();

                log.info("%03d: %s %s %s%n", responseCode, queueUrl.host(), queueUrl, responseSource);
                String contentType = response.header("Content-Type");
                if (responseCode != 200 || contentType == null) {
                    return;
                }

                MediaType mediaType = MediaType.parse(contentType);
                if (mediaType == null || !mediaType.subtype().equalsIgnoreCase("html")) {
                    this.queueService.updateError(webUrl, response);
                    return;
                }
                // should be in
                Document document = Jsoup.parse(response.body().string(), queueUrl.toString());
                log.info("finish fetch link: {}, {}", document.title(), queueUrl.url().toString());

                this.queueService.updateFetch(webUrl, response);


                for (Element element : document.select("a[href]")) {
                    String href = element.attr("href");
                    HttpUrl link = response.request().url().resolve(href);
                    if (link == null) {
                        continue; // URL is either invalid or its scheme isn't http/https.
                    }
                    if (link.host().equals(url.host())) {
                        nextLinks.add(link);
                    }
                }
                for (HttpUrl nextLink : nextLinks) {
                    //log.info("next link: {}", nextLink.url().toString());
                    WebUrl nextWebUrl = new WebUrl();
                    nextWebUrl.setUrl(nextLink.url().toString());
                    //queue.add(nextLink.newBuilder().fragment(null).build());                                  
                    //fetchEngine.send(webUrl);
                    this.queueService.start(nextWebUrl);
                }

            }
        } catch (Exception ex) {
            log.error("Exception     : {}", ex.toString());
        }
    }

}
