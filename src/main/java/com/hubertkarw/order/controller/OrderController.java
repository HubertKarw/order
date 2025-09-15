package com.hubertkarw.order.controller;

import com.hubertkarw.order.model.OrderCreateRequest;
import com.hubertkarw.order.model.OrderDTO;
import com.hubertkarw.order.model.ReceiptResponse;
import com.hubertkarw.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    @Operation(summary = "Get orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found orders", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))})
    })
    @GetMapping()
    public List<OrderDTO> getOrders() {
        log.info("GET /orders requested");
        return service.getOrders();
    }

    @Operation(summary = "Get order by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found order", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))})
    })
    @GetMapping("/{id}")
    public OrderDTO getCart(@PathVariable long id) {
        log.info("GET /orders/{} requested", id);
        return service.getOrder(id);
    }

    @Operation(summary = "Create order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created Order", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))})
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Order to create from cartId",
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderCreateRequest.class),
                    examples = @ExampleObject(value =
                            """
                                    {
                                    "cartId": 1,
                                    }
                                    """)))
                                @RequestBody OrderCreateRequest orderCreateRequest) {
        log.info("POST /carts requested body={}", orderCreateRequest);
        return service.createOrder(orderCreateRequest);
    }

    @Operation(summary = "Delete Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Removed order")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCart(@PathVariable long id) {
        log.info("DELETE /orders/{} requested", id);
        service.deleteOrder(id);
    }

    @Operation(summary = "Get order invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found order and returning invoice", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReceiptResponse.class))})
    })
    @GetMapping("/{id}/invoice")
    public String getReceiptFromOrder(@PathVariable long id) {
        log.info("GET /orders/{}/invoice requested",id);
        return service.getReceipt(id);
    }
}
