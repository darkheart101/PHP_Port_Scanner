package com.microservices.todo.consumer;

import com.microservices.todo.config.MessagingConfig;
import com.microservices.todo.dto.Todo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class User {

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue(Todo todo){
        System.out.println("Message Received from queue");

    }
}
