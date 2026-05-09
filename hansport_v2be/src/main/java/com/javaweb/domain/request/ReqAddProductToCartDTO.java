package com.javaweb.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqAddProductToCartDTO {
    private long productId;
    private long quantity;
}
