package com.payments.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import com.payments.service.PaymentService;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("test")
class PaymentServiceIntegrationTest {
    @Autowired private PaymentService paymentService;

    @Autowired private JdbcTemplate jdbcTemplate;

    @Test
    @Sql(scripts = "/data.sql")
    void testProcessPaymentWithMultipleBranches() {
        final var originBranch = "A";
        final var destinationBranch = "D";
        final var expectedResult = "A,C,E,D";
        final var actual = paymentService.processPayment(originBranch, destinationBranch);
        assertEquals(expectedResult, actual);
    }

    @AfterEach
    void cleanUpDatabase() {
        jdbcTemplate.execute("DELETE FROM branch_connection");
        jdbcTemplate.execute("DELETE FROM branch");
    }
}
