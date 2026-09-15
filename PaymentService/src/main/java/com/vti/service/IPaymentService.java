package com.vti.service;

import java.util.List;

import com.vti.client.dto.PaymentDto;
import com.vti.form.PaymentForm;
import com.vti.form.PaymentFormUpdate;

public interface IPaymentService {
    PaymentDto createPayment(PaymentForm form);
    PaymentDto getPaymentById(Long id);
    List<PaymentDto> getPaymentsByOrderId(Long orderId);
    PaymentDto updatePaymentStatus(Long id, PaymentFormUpdate form);
}