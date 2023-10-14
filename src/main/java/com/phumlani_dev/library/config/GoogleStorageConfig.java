package com.phumlani_dev.library.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;

@Component
public class GoogleStorageConfig {

    @Bean
    public static Storage initializeGoogleCloudStorage() {
        try {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new FileInputStream("src/main/resources/library-6f005-firebase-adminsdk-17zis-287127e243.json"));
            StorageOptions options = StorageOptions.newBuilder().setCredentials(credentials).build();
            return options.getService();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
