package com.payments.service;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.payments.exception.ErrorsException;
import com.payments.model.ErrorResponse;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;

@SpringBootTest
@ActiveProfiles("com")
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    void testPaymentProcessWithEmptyProps() {
        final var originBranch = "";
        final var destinationBranch = "";
        final var expectedErrors = List.of(
                ErrorResponse.builder()
                        .property("$.originBranch")
                        .description("Property is blank")
                        .build(),
                ErrorResponse.builder()
                        .property("$.destinationBranch")
                        .description("Property is blank")
                        .build()
        );

        thenThrownBy(() -> paymentService.processPayment(originBranch, destinationBranch))
                .isExactlyInstanceOf(ErrorsException.class)
                .extracting("errors")
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsExactlyElementsOf(expectedErrors);
    }

    @Test
    void testPaymentProcessWithNullProps() {
        final String originBranch = null;
        final String destinationBranch = null;

        thenThrownBy(() -> paymentService.processPayment(originBranch, destinationBranch))
                .isExactlyInstanceOf(NullPointerException.class);
    }
}
