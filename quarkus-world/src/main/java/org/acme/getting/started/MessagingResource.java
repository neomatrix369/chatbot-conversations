package org.acme.getting.started;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/")
public class MessagingResource {
    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    public static final String WHITE_BRIGHT = "\033[0;97m";
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";

    private static final String COMPUTER_NAME = "Quarkus";
    private Eliza eliza = new Eliza(COMPUTER_NAME);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/message/{messageContent}")
    public String message(@PathParam String messageContent) {
        System.out.print("");
        if (messageContent != null) {
            System.out.printf("%sOther person:%s %s%n", ANSI_PURPLE, ANSI_RESET, messageContent);
            String response_from_eliza = eliza.ask(messageContent);
            System.out.printf("%s%s:%s %s%n", ANSI_PURPLE, COMPUTER_NAME, ANSI_RESET, response_from_eliza);
            return response_from_eliza;
        }
        else {
            return "<No message passed>";
        }
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }
}