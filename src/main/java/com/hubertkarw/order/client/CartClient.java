package com.hubertkarw.order.client;

import com.hubertkarw.order.model.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "cart")
public interface CartClient {

    @GetMapping("carts/{id}")
    public CartResponse getCartById(@PathVariable long id);
}
