package com.payments.validator;

import com.payments.exception.ErrorsException;
import com.payments.model.ErrorResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public interface Validator {
    @SafeVarargs
    static void passOrThrow(Consumer<List<ErrorResponse>>... validators) {
        final var errors = new ArrayList<ErrorResponse>();
        Arrays.stream(validators).forEach(it -> it.accept(errors));
        if (!errors.isEmpty()) {
            throw new ErrorsException(errors);
        }
    }
}
