package com.alamer.springrabbitmqretry.config;

import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String INCOMING_QUEUE = "incoming_queue";
    public static final String DEAD_LETTER_QUEUE = "deadletter_queue";


    @Autowired
    private ConnectionFactory cachingConnectionFactory;

    // Setting the annotation listeners to use the jackson2JsonMessageConverter
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cachingConnectionFactory);
        factory.setMessageConverter(jackson2JsonMessageConverter());
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    @Bean
    public org.springframework.amqp.core.Queue deadletterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).ttl(10000).deadLetterExchange("")
                .deadLetterRoutingKey(INCOMING_QUEUE).build();
    }

    @Bean
    public org.springframework.amqp.core.Queue outgoingQueue() {
        return QueueBuilder.durable(INCOMING_QUEUE).deadLetterRoutingKey(DEAD_LETTER_QUEUE)
                .deadLetterExchange("").build();
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}
