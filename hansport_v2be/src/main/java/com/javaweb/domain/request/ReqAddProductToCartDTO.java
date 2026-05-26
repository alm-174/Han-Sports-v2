package com.javaweb.domain.request;

<<<<<<< HEAD
import jakarta.validation.constraints.Min;
=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqAddProductToCartDTO {
<<<<<<< HEAD
    @Min(value = 1, message = "Product id không hợp lệ")
    private long productId;

    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
=======
    private long productId;
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
    private long quantity;
}
