package com.payments.validator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.payments.model.BranchConnectionRequest;
import com.payments.model.ErrorResponse;
import com.payments.repository.BranchRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BranchConnectionRequestValidator {

    private final BranchRepository branchRepository;

    public void validate(@NonNull BranchConnectionRequest request,
                         @NonNull List<ErrorResponse> errors) {
        final var originBranch = request.getOriginBranch() != null ? request.getOriginBranch() : "";
        final var destinationBranch = request.getDestinationBranch() != null ? request.getDestinationBranch() : "";
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

        if (!destinationBranch.isEmpty() && !branchRepository.existsByName(originBranch))
            errors.add(
                    ErrorResponse
                            .builder()
                            .property(originProperty)
                            .description("Does not exist")
                            .build()
            );

        if (!destinationBranch.isEmpty() && !branchRepository.existsByName(destinationBranch))
            errors.add(
                    ErrorResponse
                            .builder()
                            .property(destinationProperty)
                            .description("Does not exist")
                            .build()
            );
    }
}
