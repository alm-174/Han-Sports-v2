package com.javaweb.domain.response.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.javaweb.domain.Cart;
import com.javaweb.domain.Product;
import com.javaweb.domain.response.cartdetail.ResCartDetailDTO;
import com.javaweb.domain.response.role.ResRoleDTO;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResCartDTO {
    private long id;
    private int sum;
    private UserCart user;
    private List<ResCartDetailDTO> cartDetails;
    private Instant createdAt;
    private Instant updatedAt;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserCart {
        private long id;
        private String email;
        private String name;
    }


}
