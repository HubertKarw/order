package com.hubertkarw.order.mapper;

import com.hubertkarw.order.model.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDTO toDTO(Order order);

    List<OrderDTO> toDTOList(List<Order> order);

    // Mapujemy tylko cartId i orderItems
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cartId", source = "id") // CartResponse.id → Order.cartId
    @Mapping(target = "orderItems", source = "cartItems")
    Order toOrderEntity(CartResponse cartResponse);

    // Mapujemy OrderItem
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "customizations", source = "customizations")
    OrderItem toOrderItemEntity(CartItemResponse cartItemResponse);

    // Mapujemy OrderItemCustomization
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderItem", ignore = true)
    OrderItemCustomization toOrderItemCustomizationEntity(CartItemCustomizationResponse customizationResponse);

    // Powiązania encji
    @AfterMapping
    default void linkOrderItems(@MappingTarget Order order) {
        if (order.getOrderItems() != null) {
            order.getOrderItems().forEach(item -> item.setOrder(order));
        }
    }

    @AfterMapping
    default void linkCustomizations(@MappingTarget OrderItem orderItem) {
        if (orderItem.getCustomizations() != null) {
            orderItem.getCustomizations().forEach(c -> c.setOrderItem(orderItem));
        }
    }
}
