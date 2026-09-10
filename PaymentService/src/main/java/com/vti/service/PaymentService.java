package com.vti.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.vti.dto.PaymentDto;
import com.vti.entity.Payments;
import com.vti.entity.enums.PaymentStatus;
import com.vti.form.PaymentForm;
import com.vti.form.PaymentFormUpdate;
import com.vti.repository.IPaymentRepository;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private IPaymentRepository paymentRepository;

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
        // ==========================================================================
        // TODO (làm sau - phần liên service): CHƯA gọi sang OrderService để verify
        // order_id có tồn tại không, và amount có khớp order.total_amount hay không.
        // ==========================================================================
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
    public List<PaymentDto> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public PaymentDto updatePaymentStatus(Long id, PaymentFormUpdate form) {
        Payments payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thanh toán id=" + id));

        payment.setStatus(form.getStatus());

        if (form.getStatus() == PaymentStatus.SUCCESS) {
            payment.setPaidAt(LocalDateTime.now());
            // TODO (làm sau): gọi PUT /orders/{orderId}/status bên OrderService, set status = PAID
        }

        return toDto(paymentRepository.save(payment));
    }
}