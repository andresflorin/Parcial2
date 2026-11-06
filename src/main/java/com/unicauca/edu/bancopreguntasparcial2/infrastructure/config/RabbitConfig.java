

package com.unicauca.edu.bancopreguntasparcial2.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "saberpro.direct.exchange";
    public static final String QUEUE = "question.submit.review.queue";
    public static final String ROUTING_KEY = "question.submit";

    @Bean
    public DirectExchange exchange() { return new DirectExchange(EXCHANGE); }

    @Bean
    public Queue queue() { return new Queue(QUEUE, true); }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}
