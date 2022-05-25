package io.helidon.examples.quickstart.se;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class ChatClient extends Endpoint {

    /**
     * This gets config from application.yaml on classpath
     * and uses "app" section.
     */
    private static final String COMPUTER_NAME = "Helidon";

    /**
     * The config value for the key {@code greeting}.
     */

    private Eliza eliza = new Eliza(COMPUTER_NAME);

    @Override
    public void onOpen(Session session, EndpointConfig EndpointConfig) {
        System.out.println("Connected!");
        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                System.out.println("Client OnMessage called '" + message + "'");
                try {
                    String eliza_response = eliza.ask(message);
                    session.getBasicRemote().sendText(eliza_response);
                } catch (IOException e) {
                    System.err.println("Unexpected exception " + e);
                }
            }
        });

    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Client OnClose called '" + closeReason + "'");
    }

    @Override
    public void onError(Session session, Throwable thr) {
        System.out.println("Client OnError called '" + thr + "'");

    }    
}
