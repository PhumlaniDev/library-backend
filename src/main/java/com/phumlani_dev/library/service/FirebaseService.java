package com.phumlani_dev.library.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FirebaseService {

    private final Storage gcsStorage;
    private static final Logger logger = LoggerFactory.getLogger(FirebaseService.class);

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
            logger.info("Firebase storage link: " + imageUrl);

            return imageUrl;
        } else {
            throw new IllegalArgumentException("Unsupported image format.");
        }
    }

//    public String uploadImage(byte[] imageData) throws IOException {
//        if (imageData == null || imageData.length == 0) {
//            throw new IllegalArgumentException("Invalid image data or format provided.");
//        }
//
//        String imageId = UUID.randomUUID().toString();
//        String gcsFilePath = "images/" + imageId;
//
//        // Get a reference to the Firebase Storage bucket
//        Storage storage = StorageClient.getInstance().bucket().getStorage();
//
//        // Create a Blob with the specified path and image data
//        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.fromGsUtilUri(gcsFilePath)).build();
//        Blob blob = storage.create(blobInfo, imageData);
//
//        // Make the image publicly accessible (optional)
//        Acl acl = Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER);
//        blob.createAcl(acl);
//
//        // Return the URL of the uploaded image
//        String imageUrl = blob.getMediaLink();
//        System.out.println("Firebase Storage link: " + imageUrl);
//
//        return imageUrl;
//    }
}
