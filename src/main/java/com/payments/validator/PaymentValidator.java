package com.payments.validator;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import com.payments.model.ErrorResponse;

import java.util.List;

@Component
public class PaymentValidator {

    public void validate(@NonNull String originBranch,
                         @NonNull String destinationBranch,
                         @NonNull List<ErrorResponse> errors) {
        final var originProperty = "$.originBranch";
        final var destinationProperty = "$.destinationBranch";
        if (originBranch.isEmpty())
            errors.add(
                    ErrorResponse
                            .builder()
                            .property(originProperty)
                            .description("Property is blank")
                            .build()
            );

        if (destinationBranch.isEmpty())
            errors.add(
                    ErrorResponse
                            .builder()
                            .property(destinationProperty)
                            .description("Property is blank")
                            .build()
            );
    }
}
