package com.payments.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorsResponse {
    private final List<ErrorResponse> errors;
}
