package com.payments.validator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.payments.model.Branch;
import com.payments.model.ErrorResponse;
import com.payments.repository.BranchRepository;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BranchValidator {

    private final BranchRepository branchRepository;
    public void validate(@NonNull Branch branch,
                         @NonNull List<ErrorResponse> errors){
        final var nameProperty = "$.name";
        final var transferCostProperty = "$.transferCost";
        if (Objects.isNull(branch.getName()) || branch.getName().isEmpty())
            errors.add(
                    ErrorResponse
                            .builder()
                            .property(nameProperty)
                            .description("Property is blank")
                            .build()
            );

        if (Objects.isNull(branch.getTransferCost()))
            errors.add(
                    ErrorResponse
                            .builder()
                            .property(transferCostProperty)
                            .description("Property is blank")
                            .build()
            );
        
        if (Objects.nonNull(branch.getTransferCost()) && branch.getTransferCost() < 0)
            errors.add(
                    ErrorResponse
                            .builder()
                            .property(transferCostProperty)
                            .description("Cannot be negative")
                            .build()
            );

        if (branchRepository.existsByName(branch.getName()))
            errors.add(
                    ErrorResponse
                            .builder()
                            .property(nameProperty)
                            .description("Already exists")
                            .build()
            );
    }
}
