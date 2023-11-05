package com.phumlani_dev.library.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.phumlani_dev.library.service.FirebaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;

@Component
public class GoogleStorageConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseService.class);

    @Bean
    public static Storage initializeGoogleCloudStorage() {
        try {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new FileInputStream("src/main/resources/library-6f005-firebase-adminsdk-17zis-287127e243.json"));
            StorageOptions options = StorageOptions.newBuilder().setCredentials(credentials).build();
            return options.getService();
        } catch (Exception e) {
            logger.error("An error occurred", e);
            return null;
        }
    }

//    @Bean
//    public FirebaseApp initializeFirebaseApp() throws IOException {
//        // Load Firebase Admin SDK credentials
//        FileInputStream serviceAccount = new FileInputStream("src/main/resources/book-library-5a143-firebase-adminsdk-budnm-73dd7544f8.json");
//
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .setStorageBucket("book-library-5a143.appspot.com")
//                .build();
//
//        return FirebaseApp.initializeApp(options);
//    }
}
