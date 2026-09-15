package com.vti.rabbitmqClient.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vti.entity.User;
import com.vti.rabbitmqClient.constants.Constants;

@Service
public class RabbitMQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendUserCreatedEvent(User user) {
        String message = user.getId() + ", " + user.getUsername() + ", " + user.getFullName() + ", " + user.getEmail();
        rabbitTemplate.convertAndSend(Constants.EXCHANGE, Constants.ROUTING_KEY_USER, message);
    }
}