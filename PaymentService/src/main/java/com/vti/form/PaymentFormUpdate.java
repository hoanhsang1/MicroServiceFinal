package com.vti.form;

import com.vti.entity.enums.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaymentFormUpdate {
    private PaymentStatus status;
    // khi status = SUCCESS, service tự set paid_at = now(), không nhận từ client
}