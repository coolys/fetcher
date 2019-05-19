/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.cooly.crawler.service;

import io.cooly.crawler.client.FetcherServiceClient;
import io.cooly.crawler.domain.WebUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.Future;
import org.springframework.scheduling.annotation.AsyncResult;
/**
 *
 * @author hungnguyendang
 */
@Service
public class CrawlerService {
    @Autowired
    private FetcherServiceClient fetcherServiceClient;

    public void start(String url) throws Exception {
        int threadCount = 20;
    }

    public void start(WebUrl weburl, QueueService queueService) throws Exception {
        Crawler crawler = new Crawler(weburl.getUrl(), fetcherServiceClient, queueService);
              
    }
    
    @Async
    public Future<WebUrl> startCrawl(WebUrl weburl, QueueService queueService) throws InterruptedException {
        Crawler crawler = new Crawler(weburl.getUrl(), fetcherServiceClient, queueService);
        return new AsyncResult<>(weburl);
    }

}
