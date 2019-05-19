/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.cooly.crawler.service;

import com.google.gson.Gson;
import io.cooly.crawler.client.FetcherServiceClient;
import io.cooly.crawler.domain.WebUrl;
import java.util.Set;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hungnguyendang
 */
public class CrawlerTest {

    public CrawlerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testHostUrl() {
        try {
            System.out.println("stasrt........");
            WebUrl webUrl = new WebUrl();
            webUrl.setUrl("http://kenh14.vn/");
            FetcherServiceClient fetcher = null;
          
           // Crawler crawler = new Crawler(webUrl.getUrl(), fetcher);
            //crawler.start();
            //crawler.drainQueue();
          //  Set<WebUrl> nextLinks = crawler.getNextLink();
            //String json = new Gson().toJson(nextLinks);
            //System.out.println(json);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Test of parallelDrainQueue method, of class Crawler.
     */
    //@Test
    public void testParallelDrainQueue() {
        System.out.println("parallelDrainQueue");
        int threadCount = 0;
        Crawler instance = null;
        //instance.parallelDrainQueue(threadCount);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of drainQueue method, of class Crawler.
     *
     * @throws java.lang.Exception
     */
    //@Test
    public void testDrainQueue() throws Exception {
        System.out.println("drainQueue");
        Crawler instance = null;
        //instance.drainQueue();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fetch method, of class Crawler.
     */
    //@Test
    public void testFetch() throws Exception {
        System.out.println("fetch");
        HttpUrl url = null;
        Crawler instance = null;
        //instance.fetch(url);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
