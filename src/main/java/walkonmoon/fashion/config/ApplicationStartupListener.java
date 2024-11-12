package walkonmoon.fashion.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static java.io.IO.println;

@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public synchronized void onApplicationEvent(ApplicationReadyEvent event) {
            println("\nThe application is ready to serve requests. Visit http://localhost:8080");
    }
}