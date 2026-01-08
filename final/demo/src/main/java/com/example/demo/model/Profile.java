package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "profiles")
public class Profile {

    @Id
    private String id;

    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private String state;
    private String city;
    private String pincode;
    private String address;
}
