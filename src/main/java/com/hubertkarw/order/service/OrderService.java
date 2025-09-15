package com.hubertkarw.order.service;

import com.hubertkarw.order.client.CartClient;
import com.hubertkarw.order.exception.OrderAppException;
import com.hubertkarw.order.mapper.OrderMapper;
import com.hubertkarw.order.model.*;
import com.hubertkarw.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final CartClient client;

    public List<OrderDTO> getOrders() {
        return mapper.toDTOList(repository.findAll());
    }

    public OrderDTO getOrder(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderAppException("Order not found", HttpStatus.NOT_FOUND));
        return mapper.toDTO(order);
    }

    public void deleteOrder(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderAppException("Order not found", HttpStatus.NOT_FOUND));
        repository.delete(order);
    }

    public String getReceipt(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderAppException("Order not found", HttpStatus.NOT_FOUND));
        ReceiptResponse receipt = new ReceiptResponse();
//
//        Order order = orderService.getOrderById(id);
        StringBuilder html = new StringBuilder("<html><body>");
        html.append("<h1>Faktura</h1>");
        html.append("<p>Order ID: ").append(order.getId()).append("</p>");
        html.append("<p>Cart ID: ").append(order.getCartId()).append("</p>");
        html.append("<table border='1'><tr><th>Produkt</th><th>Cena</th><th>Customizations</th></tr>");

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : order.getOrderItems()) {
            html.append("<tr>");
            html.append("<td>").append(item.getName()).append(" (").append(item.getType()).append(")</td>");
            html.append("<td>").append(item.getPrice()).append("</td>");
            html.append("<td>");
            for (OrderItemCustomization c : item.getCustomizations()) {
                html.append(c.getName()).append(" (").append(c.getType()).append(") +").append(c.getPrice()).append("<br>");
            }
            html.append("</td></tr>");
            total = total.add(item.getPrice());
            for (OrderItemCustomization c : item.getCustomizations()) {
                total = total.add(c.getPrice());
            }
        }

        html.append("</table>");
        html.append("<p><b>Total: ").append(total).append("</b></p>");
        html.append("</body></html>");
        return html.toString();

//        return receipt;
    }

    public OrderDTO createOrder(OrderCreateRequest orderCreateRequest) {
        CartResponse cartResponse = client.getCartById(orderCreateRequest.getCartId());
        if (cartResponse == null) {
            throw new OrderAppException("Cart does not exist", HttpStatus.NOT_FOUND);
        }
        Order order = mapper.toOrderEntity(cartResponse);
        return mapper.toDTO(repository.save(order));
    }
}
