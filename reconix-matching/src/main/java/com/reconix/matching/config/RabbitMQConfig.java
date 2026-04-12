package com.reconix.matching.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String FILE_PROCESSED_QUEUE = "q.file.processed";

    @Bean
    public Queue fileProcessedQueue() {
        // O 'durable()' garante que a fila sobreviva a reinicializações do RabbitMQ
        return QueueBuilder.durable(FILE_PROCESSED_QUEUE).build();
    }
}
