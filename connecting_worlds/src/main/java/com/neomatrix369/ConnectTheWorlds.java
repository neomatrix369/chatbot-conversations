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
        List<String> worldLoaded = new ArrayList<>(WORLDS.keySet());
        if (args.length > 0) {
            worldLoaded = Arrays.asList(args[0].split(","));
        }
        System.out.println(String.format("Worlds available: %s", worldLoaded));

        List<Map<String, String>> messages_exchanged = new ArrayList<>();

        Map<String, String> each_conversation = new HashMap<>();
        while (true) {
            for (String firstWorld : worldLoaded) {
                Map<String, String> world = WORLDS.get(firstWorld);
                String https_url = world.get("url");
                String response_format = world.get("response_format");
                String response_as_string = "";
                String theOtherWorld = getTheOtherWorld(firstWorld, worldLoaded);
                String messageFromTheOtherWorld = getMessageFromTheOtherWorld(firstWorld, each_conversation);

                try {
                    if (response_format.toLowerCase().contains("json")) {
                        HttpResponse<JsonNode> response = Unirest
                                .get(java.lang.String.format("%s%s", https_url, messageFromTheOtherWorld))
                                .asJson();
                        response_as_string = response.getBody()
                                .getObject()
                                .getString("message");
                    } else {
                        HttpResponse<String> response = Unirest
                                .get(java.lang.String.format("%s%s", https_url, messageFromTheOtherWorld))
                                .asString();
                        response_as_string = response.getBody();
                    }

                    System.out.printf("%s%s => %s%s: %s%n",
                            GREEN, theOtherWorld, firstWorld, ANSI_RESET,
                            messageFromTheOtherWorld);

                    each_conversation.put(firstWorld, response_as_string);
                    messages_exchanged.add(each_conversation);

                    System.out.printf("%n");
                } catch (Exception ex) {
                    System.out.println(String.format("%s: unable to connect with world '%s'. " +
                            "Please start that world %s to start a conversation.",
                            "\uD83D\uDE1E", firstWorld, firstWorld));
                }

                Thread.sleep(4000);
            }
        }
    }

    private static String getTheOtherWorld(String world_key, List<String> worlds) {
        for (String key : worlds) {
            if (!world_key.equals(key)) {
                return key;
            }
        }
        return world_key;
    }

    private static String getMessageFromTheOtherWorld(String world_key,
                                                      Map<String, String> each_conversation) {
        for (String key : each_conversation.keySet()) {
            if (!world_key.equals(key)) {
                return each_conversation.get(key);
            }
        }
        return getRandomGreetingMessage();
    }

    private static String getRandomGreetingMessage() {
        List<String> messages = Arrays.asList(
                "Hello", "How are you today?", "How are things?", "Are you having a good day?"
        );
        Random random = new Random();
        int random_int = random.nextInt(messages.size());
        return messages.get(random_int);
    }
}
