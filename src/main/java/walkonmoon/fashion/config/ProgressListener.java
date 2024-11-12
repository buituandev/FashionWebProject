package walkonmoon.fashion.config;

import org.springframework.stereotype.Component;

import static java.io.IO.print;

@Component
public class ProgressListener {
    private int progress = 0;

    public synchronized void updateProgress(int progress) {
        this.progress = progress;
        printProgressBar();
//        print("\rStarting application: " + progress + "%");
    }

    private void printProgressBar() {
        int barWidth = 100;
        int completedBars = (int) ((progress / 100.0) * barWidth);
        String bar = "=".repeat(completedBars) + " ".repeat(barWidth - completedBars);

        String[] spinner = {"|", "/", "-", "\\"};
        String currentSpinner = spinner[(progress / 5) % spinner.length];

        String output = String.format(
                "\rStarting application: %3d%% [%s] %s",
                progress,
                bar,
                currentSpinner
        );
        print(output);
    }

    public synchronized int getProgress() {
        return progress;
    }
}