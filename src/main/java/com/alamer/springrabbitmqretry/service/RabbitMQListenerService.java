package com.alamer.springrabbitmqretry.service;

import com.alamer.springrabbitmqretry.config.RabbitMQConfig;
import com.alamer.springrabbitmqretry.dto.IncomingMessageDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitMQListenerService {


    @RabbitListener(queues = {RabbitMQConfig.INCOMING_QUEUE})
    public void receiveIncomingMessage(IncomingMessageDto messageDto,
                                       @Header(required = false, name = "x-death") List<String> xDeath) {
        System.out.println(messageDto + ":" + (xDeath == null ? "" : xDeath));
        throw new RuntimeException("Not acquired");
    }

}
