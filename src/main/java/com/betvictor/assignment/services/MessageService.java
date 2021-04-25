package com.betvictor.assignment.services;

import com.betvictor.assignment.data.Message;
import com.betvictor.assignment.persistence.entities.MessageEntity;
import com.betvictor.assignment.persistence.repositories.MessageRepository;
import com.betvictor.assignment.data.OutputMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    public OutputMessage generateOutputMessage(Message message){
        final String uuid = UUID.randomUUID().toString();
        final String time = new SimpleDateFormat("yyyy-MMMM-dd HH:mm:ss").format(new Date());
        var outputMessage = new OutputMessage();
        outputMessage.setId(uuid);
        outputMessage.setSender(message.getSender());
        outputMessage.setRecipient(message.getRecipient());
        outputMessage.setText(message.getText());
        outputMessage.setTime(time);

        return outputMessage;
    }

    public void persistMessageInDB(OutputMessage message){
        logger.info("Persisting Data ...{}", message);
        var messageEntity = new MessageEntity();
        messageEntity.setId(message.getId());
        messageEntity.setSender(message.getSender());
        messageEntity.setRecipient(message.getRecipient());
        messageEntity.setText(message.getText());
        messageEntity.setTime(message.getTime());
        messageRepository.save(messageEntity);
    }
}
