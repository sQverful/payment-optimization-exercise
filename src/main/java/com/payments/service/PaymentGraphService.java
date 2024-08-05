package com.payments.service;

import com.payments.exception.ErrorException;
import com.payments.mapper.BranchConnectionMapper;
import com.payments.mapper.BranchMapper;
import com.payments.model.ErrorResponse;
import com.payments.repository.BranchConnectionsRepository;
import com.payments.repository.BranchRepository;
import com.payments.util.GraphUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static com.payments.util.GraphUtil.buildDirectedBranchGraph;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentGraphService {

    private final BranchConnectionsRepository branchConnectionsRepository;
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;
    private final BranchConnectionMapper branchConnectionMapper;

    @Transactional(readOnly = true)
    public byte[] downloadPaymentsGraphImg() {
        final var branchConnectionEntities = branchConnectionsRepository.findAll();
        final var branchEntities = branchRepository.findAll();
        final var branchConnections = branchConnectionMapper.toBranchConnections(branchConnectionEntities);
        final var branches = branchMapper.toBranches(branchEntities);
        final var graph = buildDirectedBranchGraph(branches, branchConnections);
        byte[] img;
        try {
            img = GraphUtil.generateGraphImage(graph);
        } catch (IOException e) {
            log.error("Error generating graph image ", e);
            throw new ErrorException(
                    ErrorResponse.builder()
                            .description("Error generating graph image")
                            .build()
            );
        }
        return img;
    }
}
