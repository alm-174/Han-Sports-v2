package com.javaweb.domain.response.product;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResProductDTO {
    private long id;
    private String name;
    private double price;


    private String detailDesc;

    private String shortDesc;
    private long quantity;
    private long sold;
    private String brand;
    private String target;
<<<<<<< HEAD
    private String category;
    private List<String> images;
=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf

    private Instant createdAt;
    private Instant updatedAt;
}
