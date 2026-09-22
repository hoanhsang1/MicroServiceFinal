package com.vti.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.vti.client.OrderClient;
import com.vti.client.dto.OrderClientDto;
import com.vti.client.dto.PaymentDto;
import com.vti.entity.Payments;
import com.vti.entity.enums.PaymentStatus;
import com.vti.form.PaymentForm;
import com.vti.form.PaymentFormUpdate;
import com.vti.repository.IPaymentRepository;

import feign.FeignException;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private IPaymentRepository paymentRepository;

    @Autowired
    private OrderClient orderClient;

    private PaymentDto toDto(Payments p) {
        return PaymentDto.builder()
                .id(p.getId())
                .orderId(p.getOrderId())
                .userId(p.getUserId())
                .amount(p.getAmount())
                .method(p.getMethod())
                .status(p.getStatus())
                .transactionCode(p.getTransactionCode())
                .paidAt(p.getPaidAt())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    @Override
    public PaymentDto createPayment(PaymentForm form) {
        OrderClientDto order;
        try {
            order = orderClient.getOrderById(form.getOrderId());
        } catch (FeignException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Đơn hàng id=" + form.getOrderId() + " không tồn tại");
        }

        if ("CANCELLED".equals(order.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Đơn hàng đã bị huỷ, không thể thanh toán");
        }
        if ("PAID".equals(order.getStatus()) || "COMPLETED".equals(order.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Đơn hàng đã được thanh toán trước đó");
        }
        if (order.getTotalAmount().compareTo(form.getAmount()) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Số tiền thanh toán (" + form.getAmount() + ") không khớp với đơn hàng (" + order.getTotalAmount() + ")");
        }

        Payments payment = Payments.builder()
                .orderId(form.getOrderId())
                .userId(form.getUserId())
                .amount(form.getAmount())
                .method(form.getMethod())
                .status(PaymentStatus.PENDING)
                .transactionCode(UUID.randomUUID().toString())
                .build();

        return toDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentDto getPaymentById(Long id) {
        Payments payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thanh toán id=" + id));
        return toDto(payment);
    }

    @Override
    public List<PaymentDto> getPaymentsByOrderId(Long orderId, Long userId, String userRole) {
        boolean isAdmin = "ADMIN".equals(userRole);
        boolean isOwner = paymentRepository.existsByOrderIdAndUserId(orderId, userId);
        if (!isAdmin && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xem các thanh toán của đơn hàng này");
        }
        return paymentRepository.findByOrderId(orderId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public PaymentDto updatePaymentStatus(Long id, PaymentFormUpdate form, String userRole) {
        Payments payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thanh toán id=" + id));

        payment.setStatus(form.getStatus());
        boolean isAdmin = "ADMIN".equals(userRole);
        if (!isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền cập nhật trạng thái thanh toán này");
        }
        if (form.getStatus() == PaymentStatus.SUCCESS) {
            payment.setPaidAt(LocalDateTime.now());
            try {
                orderClient.updateStatus(payment.getOrderId(), Map.of("status", "PAID"));
            } catch (FeignException e) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                        "Không thể cập nhật trạng thái đơn hàng id=" + payment.getOrderId());
            }
        }

        return toDto(paymentRepository.save(payment));
    }
}