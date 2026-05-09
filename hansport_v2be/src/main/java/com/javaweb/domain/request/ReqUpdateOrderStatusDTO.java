package com.javaweb.domain.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUpdateOrderStatusDTO {
    @Min(value = 1, message = "Order id không hợp lệ")
    private long id;

    @NotBlank(message = "Trạng thái không được để trống")
    private String status;
}
