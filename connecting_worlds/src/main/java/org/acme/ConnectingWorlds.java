package org.acme;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;

@Startup
@ServerEndpoint("/chat/{username}")         
@ApplicationScoped
public class ConnectingWorlds {

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    public static final String WHITE_BRIGHT = "\033[0;97m";
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE
    public static final String RED = "\033[0;31m";     // RED
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String ANSI_RESET = "\u001B[0m";

    void onStart(@Observes StartupEvent ev) {
        System.out.println("#######################################");
        System.out.println("## CONNECTING-WORLDS WEBSOCKET MODE  ##");
        System.out.println("#######################################");
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessions.put(username, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(username);
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        sessions.remove(username);
        broadcast("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        if (message.equalsIgnoreCase("_ready_")) {
            System.out.printf("%sUser %s joined%n", GREEN, username);

            broadcast("User " + username + " joined");

        } else {
            System.out.printf("%s%s%s: %s%n", GREEN, username, ANSI_RESET, message);

            if (sessions.size() > 1) {
                try {
                    Thread.sleep(2000);
                    
                    Entry<String, Session> otherWorld = getOtherWorld(username);
                    sendMessage(otherWorld.getValue(), message);

                    System.out.printf("%s%s => %s%s: %s%n", GREEN, username, otherWorld.getKey(), ANSI_RESET, message);
                
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.printf("%s%s is alone in the room...%n", GREEN, username);
            }
        }
    }

    private Entry<String, Session> getOtherWorld(String username) {
        List<Entry<String, Session>> validSessions = sessions.entrySet().stream()
                                            .filter(e -> e.getKey() != username)
                                            .collect(Collectors.toList());
        return getRandomElement(validSessions);
    }

    private <T> T getRandomElement(List<T> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            sendMessage(s, message);
        });
    }

    private void sendMessage(Session nextSession, String message) {
        nextSession.getAsyncRemote().sendObject(message, result ->  {
            if (result.getException() != null) {
                System.out.println("Unable to send message: " + result.getException());
            }
        });
    }
}