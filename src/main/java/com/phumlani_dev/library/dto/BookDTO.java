package com.phumlani_dev.library.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class BookDTO {

    private String title;
    private String author;
    private String description;
    @Lob
    @Column(columnDefinition = "VARCHAR") // Adjust the columnDefinition based on your database system
    private byte[] coverImage;
}
