package com.reconix.fraud.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String FRAUD_ALERT_EXCHANGE = "fraud.alert.exchange";
    public static final String FRAUD_ALERT_QUEUE = "fraud.alert.queue";
    public static final String FRAUD_ALERT_ROUTING_KEY = "fraud.alert";

    @Bean
    public Exchange fraudAlertExchange() {
        return ExchangeBuilder
                .topicExchange(FRAUD_ALERT_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue fraudAlertQueue() {
        return QueueBuilder
                .durable(FRAUD_ALERT_QUEUE)
                .build();
    }

    @Bean
    public Binding fraudAlertBinding() {
        return BindingBuilder
                .bind(fraudAlertQueue())
                .to(fraudAlertExchange())
                .with(FRAUD_ALERT_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
