package walkonmoon.fashion.config;

import org.springframework.stereotype.Component;

@Component
public class ProgressListener {
    private int progress = 0;

    public synchronized void updateProgress(int progress) {
        this.progress = progress;
        System.out.print("\rStarting application: " + progress + "%");
    }

    public synchronized int getProgress() {
        return progress;
    }
}