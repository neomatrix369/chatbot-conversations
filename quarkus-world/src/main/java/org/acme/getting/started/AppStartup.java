package org.acme.getting.started;

import javax.websocket.ContainerProvider;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.websocket.DeploymentException;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.Startup;


@Startup
@ApplicationScoped
public class AppStartup {

    @ConfigProperty(name = "chat.hub.username") 
    String username;

    @ConfigProperty(name = "chat.hub.host") 
    String host;

    void onStart(@Observes StartupEvent ev) {
        System.out.println("###################################");
        System.out.println("## QUARKUS-WORLD WEBSOCKET MODE  ##");
        System.out.println("###################################");

        try {
            connectToServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void onStop(@Observes ShutdownEvent ev) {               
        System.out.println("The application is stopping...");
    }

    void connectToServer() throws InterruptedException {
        // Register websocket consumers as soon the application starts
        try {
            ContainerProvider.getWebSocketContainer().connectToServer(ChatClient.class, new URI(host + username));
        } catch (DeploymentException | IOException e) {
            System.out.println("Failed to connect websocket server, retry connect in a few secs... ");
            Thread.sleep(4000);
            connectToServer();
        } catch (URISyntaxException e) {
            System.err.println("Error while connecting to websocket server: " + e.getMessage());
        }
    }
}
