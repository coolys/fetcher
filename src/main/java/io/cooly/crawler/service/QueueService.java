/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.cooly.crawler.service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import io.cooly.crawler.domain.WebUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 *
 * @author hungnguyendang
 */
@Service
public class QueueService {

    private final Logger log = LoggerFactory.getLogger(QueueService.class);
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private RabbitListenerEndpointRegistry registry;

    @Autowired
    private CrawlerService crawlerService;

    final static Gson gsonParser = new GsonBuilder()
        .registerTypeAdapter(Date.class, new JsonDeserializer() {
            public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
                return new Date(jsonElement.getAsJsonPrimitive().getAsLong());
            }
        })
        .create();

    final static Gson gson = new Gson();

    final static ObjectMapper objectMapper = new ObjectMapper();


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrentConsumers(10);
        factory.setMaxConcurrentConsumers(30);
        return factory;
    }
    
   // private final CountDownLatch latch = new CountDownLatch(1_000_000);

    public void start(WebUrl webUrl) {
        try {
            String messageContent = convertLinkToString(webUrl);
            template.convertAndSend("crawl", messageContent);
            this.registry.start();

        } catch (Exception ex) {
            log.info("QueueService ex..........: {}",ex.toString());
        }
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


    @Bean
    public Queue crawl() {
        return new Queue("crawl", false, false, true);
    }

    @RabbitListener(queues = "crawl")
    public void listen(Message message) throws Exception {
        log.info("received: {}", message);
        String content = new String(message.getBody(), "UTF-8");
        WebUrl webUrl = convertToLink(content);
        crawlerService.start(webUrl, this);

    }
}
