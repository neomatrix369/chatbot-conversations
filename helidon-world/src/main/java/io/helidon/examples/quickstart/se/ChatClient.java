package io.helidon.examples.quickstart.se;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class ChatClient extends Endpoint {

    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    public static final String WHITE_BRIGHT = "\033[0;97m";
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String ANSI_RESET = "\u001B[0m";

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

        // Register message handler
        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                try {
                    System.out.printf("%sOther world:%s %s%n", RED, ANSI_RESET, message);
                    String responseFromEliza = eliza.ask(message);
                    System.out.printf("%s%s:%s %s%n", RED, COMPUTER_NAME, ANSI_RESET, responseFromEliza);
                    session.getBasicRemote().sendText(responseFromEliza);
                } catch (IOException e) {
                    System.err.println("Unexpected exception " + e);
                }
            }
        });

        System.out.println("Send greetings to chat room...");
        try {
            session.getBasicRemote().sendText(getRandomGreetingMessage());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Client OnClose called '" + closeReason + "'");
    }

    @Override
    public void onError(Session session, Throwable thr) {
        System.out.println("Client OnError called '" + thr + "'");

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
