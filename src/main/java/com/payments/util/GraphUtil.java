package com.payments.util;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.util.mxCellRenderer;
import lombok.NonNull;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import com.payments.model.Branch;
import com.payments.model.BranchConnection;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    public static byte[] generateGraphImage(@NonNull Graph<Branch, BranchConnection> graph) throws IOException {
        JGraphXAdapter<Branch, BranchConnection> graphAdapter = new JGraphXAdapter<>(graph);
        final var layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        final var image = mxCellRenderer.createBufferedImage(
                graphAdapter,
                null,
                2,
                Color.WHITE,
                false,
                null
        );
        final var baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        final var imgResult = baos.toByteArray();
        baos.close();
        return imgResult;
    }


}
