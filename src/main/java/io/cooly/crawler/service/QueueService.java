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
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
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



    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("crawl");
        container.setConcurrentConsumers(30);
        container.setMaxConcurrentConsumers(50);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    public void start(WebUrl webUrl) {
        try {
            String messageContent = convertLinkToString(webUrl);
            template.convertAndSend("crawl", messageContent);
        } catch (Exception ex) {
            log.info("QueueService ex..........: {}",ex.toString());
        }
    }

    public static String convertLinkToString(WebUrl link) {
        if(link == null) return null;
        String content = gson.toJson(link);
        return content;

    }

    @Bean
    MessageListenerAdapter listenerAdapter(FetchBotWorker worker) {
        return new MessageListenerAdapter(worker, "receiveMessage");
    }

    @Bean
    public Queue crawl() {
        return new Queue("crawl", false, false, true);
    }

}
