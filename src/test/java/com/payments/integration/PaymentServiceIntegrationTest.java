package com.payments.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import com.payments.service.PaymentService;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("com")
@Sql({"/data.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PaymentServiceIntegrationTest {
    @Autowired
    private PaymentService paymentService;

    @Test
    void testProcessPaymentWithMultipleBranches() {;
        final var originBranch = "A";
        final var destinationBranch = "D";
        final var expectedResult = "A,C,E,D";
        final var actual = paymentService.processPayment(originBranch, destinationBranch);
        assertEquals(expectedResult, actual);
    }

}
