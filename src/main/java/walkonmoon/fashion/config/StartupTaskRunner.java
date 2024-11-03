package walkonmoon.fashion.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupTaskRunner implements ApplicationRunner {

    private final ProgressListener progressListener;

    public StartupTaskRunner(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        int totalTasks = 5; // Number of startup tasks
        for (int i = 1; i <= totalTasks; i++) {
            // Simulate a startup task
            Thread.sleep(1000); // Replace with actual startup task
            int progress = (i * 100) / totalTasks;
            progressListener.updateProgress(progress);
        }
        System.out.println("\nThe application is ready to serve requests. Visit http://localhost:8080");
    }
}