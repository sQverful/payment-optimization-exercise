package com.payments.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private String property;
    private String description;
}
