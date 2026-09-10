package com.vti.form;

import com.vti.entity.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class PaymentForm {
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private PaymentMethod method;
}