package com.payments.controller;

import com.payments.service.PaymentGraphService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final PaymentGraphService paymentGraphService;

    @PostMapping("/process")
    public ResponseEntity<String> process(@RequestBody ProcessRequest request) {;
        final var processedPayment =
                paymentService.processPayment(request.getOriginBranch(), request.getDestinationBranch());
        return ResponseEntity.ofNullable(processedPayment);
    }

    @GetMapping("/downloadGraphImage")
    public ResponseEntity<byte[]> downloadGraphImage() {
        byte[] graphData = paymentGraphService.downloadPaymentsGraphImg();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment; filename=graph.png");
        headers.setContentLength(graphData.length);
        return new ResponseEntity<>(graphData, headers, HttpStatus.OK);
    }
}
