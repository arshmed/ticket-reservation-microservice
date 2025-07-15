package com.arshmed.customerservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.customer-creation}")
    private String customerCreationQueue;
    
    @Value("${rabbitmq.exchange.name}") 
    private String internalExchange;

    @Value("${rabbitmq.routing-key.customer-creation}")
    private String customerCreationRoutingKey;

    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue customerCreationQueue() {
        return new Queue(this.customerCreationQueue);
    }

    @Bean
    public Binding internalToCustomerCreationBinding() {
        return BindingBuilder
                .bind(customerCreationQueue())
                .to(internalTopicExchange())
                .with(customerCreationRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}