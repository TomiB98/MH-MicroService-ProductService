package com.example.product_service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue rollbackQueue() {
        return new Queue("rollbackStockQueue", false);
    }

    @Bean
    public TopicExchange rollbackExchange() {
        return new TopicExchange("rollbackExchange");
    }

    @Bean
    public Binding rollbackBinding(Queue rollbackQueue, TopicExchange rollbackExchange) {
        return BindingBuilder.bind(rollbackQueue).to(rollbackExchange).with("rollback.stock");
    }
}
