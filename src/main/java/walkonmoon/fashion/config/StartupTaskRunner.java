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
        int totalTasks = 2;
        for (int i = 1; i <= totalTasks; i++) {
            Thread.sleep(1000);
            int progress = (i * 100) / totalTasks;
            progressListener.updateProgress(progress);
        }
    }
}