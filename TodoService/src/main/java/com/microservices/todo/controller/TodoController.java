package com.microservices.todo.controller;

import com.microservices.todo.config.MessagingConfig;
import com.microservices.todo.dto.Todo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/todo")
public class TodoController {

    @Autowired
    private RabbitTemplate template;


    @GetMapping(path = "/all", produces = "application/json")
    public String getTodos(){
        Todo todo = new Todo();
        System.out.println("TEST TEST TEST");
        template.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY,todo);
        return "DONE";
    }

    @GetMapping(path = "/xxx", produces = "application/json")
    public String testController(){
//        Todo todo = new Todo();
//        System.out.println("TEST TEST TEST");
//        template.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY,todo);
        return "DONE";
    }

}
