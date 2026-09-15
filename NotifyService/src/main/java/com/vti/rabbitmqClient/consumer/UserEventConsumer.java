package com.vti.rabbitmqClient.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vti.rabbitmqClient.constants.Constants;
import com.vti.rabbitmqClient.service.NotifyHandler;

@Component
public class UserEventConsumer {
    @Autowired
    private NotifyHandler notifyHandler;

    @RabbitHandler
    @RabbitListener(queues = Constants.QUEUE_USER_CREATED)
    public void receiveMessage(String message) {
        System.out.println("Received message from AuthService: " + message);
        notifyHandler.handleUserNotification(message);
    }
}