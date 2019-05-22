package io.cooly.crawler.service;


import com.google.gson.Gson;
import io.cooly.crawler.domain.WebUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FetchBotWorker {

    @Autowired
    private CrawlerService crawlerService;
    @Autowired
    private QueueService queueService;
    final static Gson gson = new Gson();

    public void receiveMessage(String message) throws Exception {
        System.out.println("Received <" + message + ">");
       // String content = new String(message, "UTF-8");
        WebUrl webUrl = convertToLink(message);
        crawlerService.start(webUrl, queueService);
    }
    public static String convertLinkToString(WebUrl link) {
        if(link == null) return null;
        String content = gson.toJson(link);
        return content;
    }

    public static WebUrl convertToLink(String message) {
        if(message == null) return null;
        WebUrl content = gson.fromJson(message, WebUrl.class);
        return content;

    }
}
