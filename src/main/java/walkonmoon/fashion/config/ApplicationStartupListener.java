package walkonmoon.fashion.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static java.io.IO.println;

@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    private final ProgressListener progressListener;
    private boolean isMessagePrinted = false;

    public ApplicationStartupListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public synchronized void onApplicationEvent(ApplicationReadyEvent event) {
        if (!isMessagePrinted) {
            progressListener.updateProgress(100);
            println("\nThe application is ready to serve requests. Visit http://localhost:8080");
            isMessagePrinted = true;
        }
    }
}