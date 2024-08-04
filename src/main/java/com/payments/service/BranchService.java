package com.payments.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.payments.entity.BranchConnectionEntity;
import com.payments.mapper.BranchConnectionMapper;
import com.payments.mapper.BranchMapper;
import com.payments.model.Branch;
import com.payments.model.BranchConnection;
import com.payments.model.BranchConnectionRequest;
import com.payments.repository.BranchConnectionsRepository;
import com.payments.repository.BranchRepository;
import com.payments.validator.BranchConnectionRequestValidator;
import com.payments.validator.BranchValidator;

import java.util.List;

import static com.payments.validator.Validator.passOrThrow;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchConnectionsRepository branchConnectionsRepository;
    private final BranchRepository branchRepository;
    private final BranchConnectionMapper branchConnectionMapper;
    private final BranchMapper branchMapper;
    private final BranchConnectionRequestValidator branchConnectionRequestValidator;
    private final BranchValidator branchValidator;

    public List<Branch> getAllBranches() {
        final var branchEntities = branchRepository.findAll();
        return branchMapper.toBranches(branchEntities);
    }

    public List<BranchConnection> getAllBranchConnections() {
        final var branchConnectionEntities = branchConnectionsRepository.findAll();
        return branchConnectionMapper.toBranchConnections(branchConnectionEntities);
    }

    public Branch createBranch(@NonNull Branch branch) {
        passOrThrow(errors -> branchValidator.validate(branch, errors));
        final var branchEntity = branchMapper.toBranchEntity(branch);
        branchRepository.save(branchEntity);
        return branchMapper.toBranch(branchEntity);
    }

    public BranchConnection createBranchConnection(@NonNull BranchConnectionRequest request) {
        passOrThrow(errors -> branchConnectionRequestValidator.validate(request, errors));
        final var sourceBranchEntity = branchRepository.findByName(request.getOriginBranch())
                .orElseThrow();
        final var destinationBranchEntity = branchRepository.findByName(request.getDestinationBranch()).orElseThrow();
        final var branchConnectionEntity = BranchConnectionEntity.builder()
                .originBranch(sourceBranchEntity)
                .destinationBranch(destinationBranchEntity)
                .build();
        branchConnectionsRepository.save(branchConnectionEntity);
        return branchConnectionMapper.toBranchConnection(branchConnectionEntity);
    }

}
