package com.javaweb.domain.request;

<<<<<<< HEAD
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReqOrderDTO {
    @NotBlank(message = "Tên không được để trống")
    String receiverName;

    @NotBlank(message = "Số điện thoại không được để trống")
    String receiverPhone;

    @NotBlank(message = "Địa chỉ không được để trống")
    String receiverAddress;

    List<Long> cartDetailIds;
=======
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqOrderDTO {
    @NotNull(message = "Tên không được để trống")
    String receiverName;
    @NotNull(message = "Số điện thoại không được để trống")
    String receiverPhone;
    @NotNull(message = "Địa chỉ không được để trống")
    String receiverAddress;
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
}
