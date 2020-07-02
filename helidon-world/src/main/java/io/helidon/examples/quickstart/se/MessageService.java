/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.helidon.examples.quickstart.se;

import io.helidon.config.Config;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * A simple service to greet you. Examples:
 *
 * Get default greeting message:
 * curl -X GET http://localhost:8080/message/<your-message>
 *
 * The message is returned as a JSON object
 */

public class MessageService implements Service {
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

    /**
     * A service registers itself by updating the routine rules.
     * @param rules the routing rules.
     */
    @Override
    public final void update(final Routing.Rules rules) {
        rules
            .get("/{message}", this::getMessage);
    }

    /**
     * Return a greeting message using the name that was provided.
     * @param request the server request
     * @param response the server response
     */
    private void getMessage(final ServerRequest request,
                            final ServerResponse response) {
        String message = request.path().param("message");
        System.out.print("");
        System.out.printf("%sOther world:%s %s%n", RED, ANSI_RESET, message);
        String eliza_response = eliza.ask(message);
        System.out.printf("%s%s%s: %s%n", RED, COMPUTER_NAME, ANSI_RESET, eliza_response);

        JsonObject returnObject = Json.createObjectBuilder()
                .add("message", eliza_response)
                .build();
        response.send(returnObject);
    }
}
