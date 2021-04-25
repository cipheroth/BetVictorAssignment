package com.betvictor.assignment.controllers;

import com.betvictor.assignment.data.Message;
import com.betvictor.assignment.data.OutputMessage;
import com.betvictor.assignment.services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    MessageService messageService;

    @Autowired private SimpMessagingTemplate simpMessagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @MessageMapping("/chat/{user}")
    public OutputMessage simple(@DestinationVariable String user, Message message) throws Exception {

        logger.info("Message Arrived for [User: {}]", user);
        var outputMessage = messageService.generateOutputMessage(message);
        messageService.persistMessageInDB(outputMessage);

        simpMessagingTemplate.convertAndSend("/topic/message/"+user, outputMessage);
        simpMessagingTemplate.convertAndSend("/topic/message/action-monitor", outputMessage);

        return outputMessage;
    }
}
