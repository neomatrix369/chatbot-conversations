 // Copyright 2020 Mani Sarkar

 // Licensed under the Apache License, Version 2.0 (the "License");
 // you may not use this file except in compliance with the License.
 // You may obtain a copy of the License at

 //    http://www.apache.org/licenses/LICENSE-2.0

 // Unless required by applicable law or agreed to in writing, software
 // distributed under the License is distributed on an "AS IS" BASIS,
 // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 
 // See the License for the specific language governing permissions and
 // limitations under the License.

package com.neomatrix369;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.util.*;

/**
 * Hello world!
 */
public class ConnectTheWorlds {
    private static final Map<String, Map<String, String>> WORLDS =
            Map.of("Helidon", Map.of("url", "http://localhost:9090/message/",
                    "response_format", "application/json"),
                    "Quarkus", Map.of("url", "http://localhost:8080/message/",
                            "response_format", "application/text"),
                    "Roberta", Map.of("url", "http://localhost:7070/send?message=",
                            "response_format", "application/text")
            );

    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    public static final String WHITE_BRIGHT = "\033[0;97m";
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE
    public static final String RED = "\033[0;31m";     // RED
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) throws InterruptedException {
        System.out.println(String.format("Args passed in: '%s'", String.join(",", args)));

        List<String> worldsLoaded = new ArrayList<>(WORLDS.keySet());

        if ((args != null) && (args.length > 0)) {
            if (args[0].trim().length() > 0) {
                List<String> worldsPassedIn = Arrays.asList(args[0].split(","));
                worldsLoaded = worldsPassedIn;
            }
        }

        System.out.println(String.format("Worlds available: %s", worldsLoaded));

        List<Map<String, String>> messages_exchanged = new ArrayList<>();

        Map<String, String> each_conversation = new HashMap<>();
        while (true) {
            for (String firstWorldKey : worldsLoaded) {
                Map<String, String> firstWorld = WORLDS.get(firstWorldKey);
                String https_url = firstWorld.get("url");
                String response_format = firstWorld.get("response_format");
                String responseToFirstWorldFromOtherWorld = "";
                String theOtherWorld = getTheOtherWorld(firstWorldKey, worldsLoaded);
                String messageFromOtherWorld = getMessageFromTheOtherWorld(theOtherWorld, each_conversation);

                try {
                    if (response_format.toLowerCase().contains("json")) {
                        HttpResponse<JsonNode> response = Unirest
                                .get(java.lang.String.format("%s%s", https_url, messageFromOtherWorld))
                                .asJson();
                        responseToFirstWorldFromOtherWorld = response.getBody()
                                .getObject()
                                .getString("message");
                    } else {
                        HttpResponse<String> response = Unirest
                                .get(java.lang.String.format("%s%s", https_url, messageFromOtherWorld))
                                .asString();
                        responseToFirstWorldFromOtherWorld = response.getBody();
                    }

                    System.out.printf("%s%s => %s%s: %s%n",
                            GREEN, theOtherWorld, firstWorldKey, ANSI_RESET,
                            messageFromOtherWorld);

                    each_conversation.put(firstWorldKey, responseToFirstWorldFromOtherWorld);
                    messages_exchanged.add(each_conversation);

                    System.out.printf("%n");
                } catch (Exception ex) {
                    System.out.println(String.format("%s: unable to connect with world '%s'. " +
                            "Please start that world %s to start a conversation.",
                            "\uD83D\uDE1E", firstWorldKey, firstWorldKey));
                }

                Thread.sleep(4000);
            }
        }
    }

    private static String getTheOtherWorld(String world_key, List<String> worlds) {
        List<String> copy_of_worlds = new ArrayList<>(worlds);
        copy_of_worlds.remove(world_key);
        if (copy_of_worlds.size() == 1) {
            return copy_of_worlds.get(0);
        }
        int randomIndex = getRandomNumber(copy_of_worlds.size());
        return copy_of_worlds.get(randomIndex);
    }

    private static String getMessageFromTheOtherWorld(String world_key,
                                                      Map<String, String> each_conversation) {
        String message = null;
        if (each_conversation.size() > 0) {
            message = each_conversation.get(world_key);
        }

        return message == null ? getRandomGreetingMessage() : message;
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
