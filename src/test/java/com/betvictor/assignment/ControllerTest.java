package com.betvictor.assignment;

import com.betvictor.assignment.data.Message;
import com.betvictor.assignment.data.OutputMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTest {

    @Value("${local.server.port}")
    private int port;
    private String URL;

    private static final String CHAT_ENDPOINT_USER_ODIN = "/app/chat/odin";
    private static final String SUBSCRIPTION_ENDPOINT_ODIN = "/topic/message/odin";
    private static final String SUBSCRIPTION_ACTION_MONITOR = "/topic/message/action-monitor";

    private CompletableFuture<OutputMessage> messageProcessed;

    @Before
    public void setup(){
        messageProcessed = new CompletableFuture<>();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>PORT " + port);
        URL = "ws://localhost:" + port + "/chat";
    }

    @Test
    public void shouldSendAndReceiveChatMessage() throws Exception{
        var stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        var stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {
        }).get(3, SECONDS);

        var senderString = "thor";
        var recipientString = "odin";
        var textMessageString = "Ragnarok!!";

        Message message = new Message();
        message.setSender(senderString);
        message.setRecipient(recipientString);
        message.setText(textMessageString);

        stompSession.subscribe(SUBSCRIPTION_ENDPOINT_ODIN, new CreateStompFrameHandler());
        stompSession.send(CHAT_ENDPOINT_USER_ODIN,  message);

        OutputMessage messageProcessedByController = messageProcessed.get(10, SECONDS);

        assertNotNull(messageProcessedByController);
        assertNotNull(messageProcessedByController.getId());
        assertNotNull(messageProcessedByController.getTime());
        assertEquals(senderString, messageProcessedByController.getSender());
        assertEquals(recipientString, messageProcessedByController.getRecipient());
        assertEquals(textMessageString, messageProcessedByController.getText());

    }

    @Test
    public void shouldReceiveActionEvent() throws Exception {
        var stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        var senderString = "freya";
        var recipientString = "odin";
        var textMessageString = "Some mead in Valhalla";

        Message message = new Message();
        message.setSender(senderString);
        message.setRecipient(recipientString);
        message.setText(textMessageString);


        var stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {}).get(3, SECONDS);
        stompSession.subscribe(SUBSCRIPTION_ACTION_MONITOR, new CreateStompFrameHandler());
        stompSession.send(CHAT_ENDPOINT_USER_ODIN, message);

        var messageProcessedByController = messageProcessed.get(10, SECONDS);

        assertNotNull(messageProcessedByController);
        assertNotNull(messageProcessedByController.getId());
        assertNotNull(messageProcessedByController.getTime());
        assertEquals(senderString, messageProcessedByController.getSender());
        assertEquals(recipientString, messageProcessedByController.getRecipient());
        assertEquals(textMessageString, messageProcessedByController.getText());
    }

    private List<Transport>  createTransportClient(){
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        return transports;
    }

    private class CreateStompFrameHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders stompHeaders){
            return OutputMessage.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object object){
            messageProcessed.complete((OutputMessage)object);
        }
    }

}
