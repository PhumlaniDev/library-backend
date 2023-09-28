package com.phumlani_dev.library.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long addressId;
    private String streetNumber;
    private String suburb;
    private String city;
    private String province;
    private String country;

}
