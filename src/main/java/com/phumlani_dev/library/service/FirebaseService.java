package com.phumlani_dev.library.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FirebaseService {
    private final Storage gcsStorage;

    public String uploadImage(byte[] imageData) throws IOException {
        if (imageData == null || imageData.length == 0) {
            throw new IllegalArgumentException("Invalid image data or format provided.");
        }

        // Get the MIME type of the image from its byte array
        String mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageData));

        // Validate that the detected MIME type starts with "image/"
        if (mimeType != null && mimeType.startsWith("image/")) {
            String imageExtension = mimeType.substring("image/".length());
            String imageId = UUID.randomUUID().toString();
            String gcsFilePath = "images/" + imageId + "." + imageExtension;
            BlobInfo blobInfo = BlobInfo.newBuilder("library-6f005.appspot.com", gcsFilePath).build();
            Blob blob = gcsStorage.create(blobInfo, imageData);

            // Get the public URL of the uploaded image
            String imageUrl = blob.getMediaLink();
            System.out.println("link: " + imageUrl);

            return imageUrl;
        } else {
            throw new IllegalArgumentException("Unsupported image format.");
        }
    }
}
