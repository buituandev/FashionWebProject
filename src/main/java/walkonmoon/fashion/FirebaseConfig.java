package walkonmoon.fashion;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FirebaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

    // Relative path to the secret file
    private static final String FIREBASE_CONFIG_RELATIVE_PATH = "etc/secrets/firebase-config.json";

    @PostConstruct
    public void initialize() {
        try {
            Path rootPath = Paths.get("").toAbsolutePath();
            Path configPath = rootPath.resolve(FIREBASE_CONFIG_RELATIVE_PATH);

            if (!Files.exists(configPath)) {
                throw new IOException("Configuration file not found: " + configPath.toString());
            }

            try (FileInputStream serviceAccount = new FileInputStream(configPath.toFile())) {
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setStorageBucket(getBucketName())
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                    logger.info("Firebase initialized successfully.");
                } else {
                    logger.info("FirebaseApp already initialized.");
                }
            }
        } catch (IOException e) {
            logger.error("Failed to initialize Firebase: ", e);
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }

    public String getBucketName() {
        return "chromashop.appspot.com";
    }
}