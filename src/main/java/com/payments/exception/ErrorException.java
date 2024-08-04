package com.payments.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.payments.model.ErrorResponse;

@RequiredArgsConstructor
@Getter
public class ErrorException extends RuntimeException {
    private final transient ErrorResponse error;
}

