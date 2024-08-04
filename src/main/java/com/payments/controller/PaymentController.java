package com.payments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.payments.model.ProcessRequest;
import com.payments.service.PaymentService;

@RestController
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<String> process(@RequestBody ProcessRequest request) {
        final var processedPayment =
                paymentService.processPayment(request.getOriginBranch(), request.getDestinationBranch());
        return ResponseEntity.ofNullable(processedPayment);
    }
}
