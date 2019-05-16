/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.cooly.crawler.service;

import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

/**
 *
 * @author hungnguyendang
 */
@Service
public class PerfService {

    private final Logger log = LoggerFactory.getLogger(PerfService.class);
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private RabbitListenerEndpointRegistry registry;

    private final CountDownLatch latch = new CountDownLatch(1_000_000);

    public void start() {
        try {
            log.info("start..........");
            for (int i = 0; i < 1_000_000; i++) {
                //log.info("send..........");
                template.convertAndSend("perf", "foo");
            }
            StopWatch watch = new StopWatch();
            watch.start();
            this.registry.start();
            this.latch.await();
            watch.stop();
            log.info("perf==============={}", watch.getTotalTimeMillis() + " rate: " + 1_000_000_000.0 / watch.getTotalTimeMillis());
        } catch (IllegalStateException | InterruptedException | AmqpException ex) {
            log.info("PerfService ex..........: {}",ex.toString());
        }
    }

    @RabbitListener(queues = "perf")
    public void listen(Message message) {
        //log.info("received: {}", message);
        this.latch.countDown();
    }
}
