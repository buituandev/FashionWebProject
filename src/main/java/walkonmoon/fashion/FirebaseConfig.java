package walkonmoon.fashion;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    private static final String FIREBASE_CONFIG_ENV = "FIREBASE_CONFIG"; // Environment variable name

    @PostConstruct
    public void initialize() throws IOException {
        String base64Config = System.getenv(FIREBASE_CONFIG_ENV);

        if (base64Config == null || base64Config.isEmpty()) {
            throw new IllegalStateException("Firebase configuration not found in environment variable: " + FIREBASE_CONFIG_ENV);
        }

        // Decode the base64-encoded string
        byte[] decodedConfig = Base64.getDecoder().decode(base64Config);

        try (ByteArrayInputStream serviceAccount = new ByteArrayInputStream(decodedConfig)) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(getBucketName())
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        }
    }

    public String getBucketName() {
        return "chromashop.appspot.com";
    }
}
