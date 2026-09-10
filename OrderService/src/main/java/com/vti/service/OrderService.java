package com.vti.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.vti.dto.OrderDto;
import com.vti.dto.OrderItemDto;
import com.vti.entity.Order;
import com.vti.entity.OrderItems;
import com.vti.entity.enums.OrderStatus;
import com.vti.form.OrderForm;
import com.vti.form.OrderFormUpdate;
import com.vti.form.OrderItemForm;
import com.vti.repository.IOrderRepository;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    private OrderDto toDto(Order order) {
        List<OrderItemDto> items = order.getItems() == null ? List.of() :
                order.getItems().stream()
                        .map(i -> OrderItemDto.builder()
                                .id(i.getId())
                                .productId(i.getProductId())
                                .quantity(i.getQuantity())
                                .price(i.getPrice())
                                .subtotal(i.getSubtotal())
                                .build())
                        .toList();

        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .shippingAddress(order.getShippingAddress())
                .items(items)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public OrderDto createOrder(OrderForm form) {
        Order order = Order.builder()
                .userId(form.getUserId())
                .shippingAddress(form.getShippingAddress())
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItems> orderItems = new ArrayList<>();

        for (OrderItemForm itemForm : form.getItems()) {
            // ==========================================================================
            // TODO (làm sau - phần liên service): CHƯA gọi sang ProductService.
            // Cần gọi GET /products/{id} lấy price thật + kiểm tra tồn kho,
            // rồi PATCH /products/{id}/quantity (delta âm) để trừ kho.
            // Hiện đang để price = 0 nên total_amount SẼ SAI — chỉ để test cấu trúc CRUD trước.
            // ==========================================================================
            BigDecimal price = BigDecimal.ZERO;
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(itemForm.getQuantity()));
            total = total.add(subtotal);

            orderItems.add(OrderItems.builder()
                    .order(order)
                    .productId(itemForm.getProductId())
                    .quantity(itemForm.getQuantity())
                    .price(price)
                    .subtotal(subtotal)
                    .build());
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        return toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng id=" + id));
        return toDto(order);
    }

    @Override
    public List<OrderDto> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public OrderDto updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng id=" + id));
        order.setStatus(status);
        return toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto updateOrder(Long id, OrderFormUpdate form) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng id=" + id));
        if (form.getShippingAddress() != null) {
            order.setShippingAddress(form.getShippingAddress());
        }
        return toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng id=" + id));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chỉ có thể huỷ đơn khi đang PENDING");
        }

        // TODO (làm sau): hoàn lại tồn kho bên ProductService cho từng item trong order.getItems()

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}