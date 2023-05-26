package org.acme.getting.started;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.Session;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ClientEndpoint
public class ChatClient {

     // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    public static final String WHITE_BRIGHT = "\033[0;97m";
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";
    
    private static final String COMPUTER_NAME = "Quarkus";
    private Eliza _eliza = new Eliza(COMPUTER_NAME);

    @ConfigProperty(name = "chat.hub.username") 
    String username;
   
    @OnOpen
    public void open(Session session) {
        System.out.println("Connected!");
        
        System.out.println("Send greetings to chat room...");
        session.getAsyncRemote().sendText(getRandomGreetingMessage());
    }

    @OnMessage
    public void message(String msg, Session session) {
        System.out.printf("%sOther world:%s %s%n", ANSI_PURPLE, ANSI_RESET, msg);
        String responseFromEliza = _eliza.ask(msg);
        System.out.printf("%s%s:%s %s%n", ANSI_PURPLE, COMPUTER_NAME, ANSI_RESET, responseFromEliza);
        session.getAsyncRemote().sendText(responseFromEliza);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println(String.format("Session %s close", session.getId()));
    }

    private static String getRandomGreetingMessage() {
        List<String> messages = Arrays.asList(
                "Hello", "How are you today?", "How are things?", "Are you having a good day?"
        );
        int randomIndex = getRandomNumber(messages.size());
        return messages.get(randomIndex);
    }

    private static int getRandomNumber(int maxValue) {
        return new Random().nextInt(maxValue);
    }
}
