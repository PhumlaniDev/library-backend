package com.phumlani_dev.library.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue
    private Long bookId;
    private String title;
    private String author;
    private String description;
    @Column(columnDefinition = "VARCHAR") // Adjust the columnDefinition based on your database system
    private String coverImage;
}
