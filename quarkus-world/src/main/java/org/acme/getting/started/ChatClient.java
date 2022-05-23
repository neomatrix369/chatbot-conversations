package org.acme.getting.started;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.Session;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ClientEndpoint
public class ChatClient {
    
    private static final String COMPUTER_NAME = "Quarkus";

    private Eliza _eliza = new Eliza(COMPUTER_NAME);

    @ConfigProperty(name = "chat.hub.username") 
    String username;
   
    @OnOpen
    public void open(Session session) {
        System.out.println("Connected!");
    }

    @OnMessage
    public void message(String msg, Session session) {
        System.out.println(msg);
        // Ignore self message and only answer others messages.
        if (!msg.startsWith(username)) {
            String answer = _eliza.ask(msg);
            session.getAsyncRemote().sendText(answer);
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println(String.format("Session %s close", session.getId()));
    }
}
