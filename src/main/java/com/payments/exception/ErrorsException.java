package com.payments.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.payments.model.ErrorResponse;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ErrorsException extends RuntimeException {
    private final transient List<ErrorResponse> errors;
}

