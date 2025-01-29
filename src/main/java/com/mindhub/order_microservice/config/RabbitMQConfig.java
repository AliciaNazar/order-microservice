package com.mindhub.order_microservice.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Configuration
public class RabbitMQConfig {


    @Bean
    public TopicExchange mailExchange() {
        return new TopicExchange("email-exchange");
    }

    @Bean
    public Queue pdfQueue() {
        return new Queue("orderCreatedEvent", true);
    }

    @Bean
    public Binding pdfBinding(Queue pdfQueue, TopicExchange mailExchange) {
        return BindingBuilder.bind(pdfQueue).to(mailExchange).with("user.pdf");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}