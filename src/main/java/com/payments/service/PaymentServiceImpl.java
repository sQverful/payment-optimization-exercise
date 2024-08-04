package com.payments.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;
import com.payments.mapper.BranchConnectionMapper;
import com.payments.mapper.BranchMapper;
import com.payments.model.Branch;
import com.payments.model.BranchConnection;
import com.payments.repository.BranchConnectionsRepository;
import com.payments.repository.BranchRepository;
import com.payments.validator.PaymentValidator;

import java.util.Optional;

import static com.payments.util.GraphUtil.buildDirectedBranchGraph;
import static com.payments.util.GraphUtil.mapVertexPathToCsvFormat;
import static com.payments.validator.Validator.passOrThrow;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BranchConnectionsRepository branchConnectionsRepository;
    private final BranchRepository branchRepository;
    private final PaymentValidator paymentValidator;
    private final BranchMapper branchMapper;
    private final BranchConnectionMapper branchConnectionMapper;

    @Override
    public String processPayment(@NonNull String originBranch,
                                 @NonNull String destinationBranch) {
        passOrThrow(errors -> paymentValidator.validate(originBranch, destinationBranch, errors));

        final var branchConnectionEntities = branchConnectionsRepository.findAll();
        final var branchEntities = branchRepository.findAll();
        final var branchConnections = branchConnectionMapper.toBranchConnections(branchConnectionEntities);
        final var branches = branchMapper.toBranches(branchEntities);

        final var originBranchEntity = branchRepository.findByName(originBranch).orElseThrow();
        final var destinationBranchEntity = branchRepository.findByName(destinationBranch).orElseThrow();

        final var origin = branchMapper.toBranch(originBranchEntity);
        final var destination = branchMapper.toBranch(destinationBranchEntity);

        Graph<Branch, BranchConnection> graph = buildDirectedBranchGraph(branches, branchConnections);

        Optional<GraphPath<Branch, BranchConnection>> path = findCheapestPathBetweenBranches(graph, origin, destination);

        return path.map(mapVertexPathToCsvFormat())
                .orElse(null);
    }


    private Optional<GraphPath<Branch, BranchConnection>> findCheapestPathBetweenBranches(
            @NonNull Graph<Branch, BranchConnection> graph,
            @NonNull Branch originBranch,
            @NonNull Branch destinationBranch) {
        GraphPath<Branch, BranchConnection> path = new DijkstraShortestPath<>(graph)
                .getPaths(originBranch)
                .getPath(destinationBranch);
        return Optional.ofNullable(path);
    }

}
