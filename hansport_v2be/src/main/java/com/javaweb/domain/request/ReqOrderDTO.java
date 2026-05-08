package com.javaweb.domain.request;

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
}
