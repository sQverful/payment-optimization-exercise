package com.payments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payments.service.PaymentGraphService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.payments.model.ProcessRequest;
import com.payments.service.PaymentService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean private PaymentService paymentService;
    @MockitoBean private PaymentGraphService paymentGraphService;

    @Autowired JacksonTester<ProcessRequest> processRequestJacksonTester;

    @Test
    void testPaymentProcess() throws Exception {
        final var request = ProcessRequest.builder()
                .originBranch("A")
                .destinationBranch("C")
                .build();
        final var json = new ObjectMapper().writeValueAsString(request);
        final var expected = "A,B,C";

        when(paymentService.processPayment(request.getOriginBranch(), request.getDestinationBranch()))
                .thenReturn(expected);

        final var actualResponse = mockMvc.perform(post("/api/v1/payment/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        assertEquals(expected, actualResponse);
    }

}
