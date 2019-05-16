/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.cooly.crawler.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author hungnguyendang
 */
@Configuration
public class RabbitConfig {
    @Bean
	public Queue perf() {
		return new Queue("perf", false, false, true);
	}
}
