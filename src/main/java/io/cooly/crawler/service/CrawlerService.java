/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.cooly.crawler.service;

import io.cooly.crawler.domain.WebUrl;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

/**
 *
 * @author hungnguyendang
 */
@Service
public class CrawlerService {

    public void start(String url) throws Exception {
        int threadCount = 20;
        OkHttpClient client = new OkHttpClient.Builder()
                //.cache(cache)
                .build();
        Crawler crawler = new Crawler(client, url);
        crawler.drainQueue();
        crawler.parallelDrainQueue(threadCount);

    }

    public void start(WebUrl weburl) throws Exception {
        int threadCount = 20;
        OkHttpClient client = new OkHttpClient.Builder()
                //.cache(cache)
                .build();
        Crawler crawler = new Crawler(client, weburl.getUrl());
        crawler.drainQueue();
        crawler.parallelDrainQueue(threadCount);
    }

}
