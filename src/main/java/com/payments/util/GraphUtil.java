package com.payments.util;

import lombok.NonNull;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import com.payments.model.Branch;
import com.payments.model.BranchConnection;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GraphUtil {

    private GraphUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static Graph<Branch, BranchConnection> buildEmptySimpleGraph() {
        return GraphTypeBuilder
                .<Branch, BranchConnection>directed()
                .allowingMultipleEdges(true)
                .allowingSelfLoops(false)
                .edgeClass(BranchConnection.class)
                .weighted(true)
                .buildGraph();
    }

    public static Graph<Branch, BranchConnection> buildDirectedBranchGraph(@NonNull List<Branch> branches,
                                                                           @NonNull List<BranchConnection> branchConnections) {
        Graph<Branch, BranchConnection> graph = buildEmptySimpleGraph();
        branches.forEach(graph::addVertex);
        branchConnections.forEach(branchConnection -> graph.addEdge(branchConnection.getOriginBranch(),
                branchConnection.getDestinationBranch(), branchConnection));
        branchConnections.forEach(branchConnection -> graph.setEdgeWeight(branchConnection.getOriginBranch(),
                branchConnection.getDestinationBranch(),
                branchConnection.getOriginBranch().getTransferCost()));
        return graph;
    }

    public static Function<GraphPath<Branch, BranchConnection>, String> mapVertexPathToCsvFormat() {
        return path -> path.getVertexList().stream()
                .map(Branch::getName)
                .collect(Collectors.joining(","));
    }
}
