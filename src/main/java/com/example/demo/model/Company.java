package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(min = 3, max = 30)
    private String name;
    @NotEmpty
    private String image;
    @Length(min = 5, max = 50)
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @Size(min = 3, max = 30) String getName() {
        return name;
    }

    public void setName(@Size(min = 3, max = 30) String name) {
        this.name = name;
    }

    public @NotEmpty String getImage() {
        return image;
    }

    public void setImage(@NotEmpty String image) {
        this.image = image;
    }

    public @Length(min = 5, max = 50) String getAddress() {
        return address;
    }

    public void setAddress(@Length(min = 5, max = 50) String address) {
        this.address = address;
    }
}
