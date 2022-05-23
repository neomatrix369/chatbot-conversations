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
        // Register websocket consumers as soon the application starts
        try {
            ContainerProvider.getWebSocketContainer().connectToServer(ChatClient.class, new URI(host + username));
        } catch (DeploymentException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    void onStop(@Observes ShutdownEvent ev) {               
        System.out.println("The application is stopping...");
    }
}
