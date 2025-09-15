package com.hubertkarw.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemCustomizationResponse {
    private Long id;
    private String name;
    private String type;
    private BigDecimal price;
}
