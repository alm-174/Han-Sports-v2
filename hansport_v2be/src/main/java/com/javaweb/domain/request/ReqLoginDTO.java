package com.javaweb.domain.request;

<<<<<<< HEAD
import jakarta.validation.constraints.NotBlank;
=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqLoginDTO {
<<<<<<< HEAD
    @NotBlank(message = "Email không được để trống")
    private String username;

    @NotBlank(message = "Password không được để trống")
=======
    private String username;
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
    private String password;

}
