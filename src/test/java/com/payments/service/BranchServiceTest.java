package com.payments.service;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.payments.entity.BranchConnectionEntity;
import com.payments.entity.BranchEntity;
import com.payments.exception.ErrorsException;
import com.payments.model.Branch;
import com.payments.model.BranchConnection;
import com.payments.model.BranchConnectionRequest;
import com.payments.model.ErrorResponse;
import com.payments.repository.BranchConnectionsRepository;
import com.payments.repository.BranchRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("com")
class BranchServiceTest {

    @MockitoBean
    private BranchRepository branchRepository;

    @MockitoBean
    private BranchConnectionsRepository branchConnectionsRepository;


    @Autowired
    private BranchService branchService;

    @Test
    void testGetAllBranches() {
        final var branchEntities = Arrays.asList(
                new BranchEntity(1, "A", 5),
                new BranchEntity(2, "B", 6),
                new BranchEntity(3, "C", 7)
        );

        when(branchRepository.findAll()).thenReturn(branchEntities);

        final var expectedBranches = Arrays.asList(
                new Branch("A", 5),
                new Branch("B", 6),
                new Branch("C", 7));

        final var result = branchService.getAllBranches();

        assertEquals(expectedBranches, result);
    }

    @Test
    void testGetAllBranchConnections() {
        final var originBranchEntity = new BranchEntity(1, "A", 5);
        final var destinationBranchEntity = new BranchEntity(2, "B", 6);
        final var destinationBranchEntity2 = new BranchEntity(3, "C", 6);

        final var originBranchModel = new Branch(originBranchEntity.getName(), originBranchEntity.getTransferCost());
        final var destinationBranchModel = new Branch(destinationBranchEntity.getName(), destinationBranchEntity.getTransferCost());
        final var destinationBranchModel2 = new Branch(destinationBranchEntity2.getName(), destinationBranchEntity2.getTransferCost());

        final var branchConnectionEntities = List.of(
                new BranchConnectionEntity(1, originBranchEntity, destinationBranchEntity),
                new BranchConnectionEntity(1, originBranchEntity, destinationBranchEntity2)
        );

        final var expectedBranchConnections = List.of(
                new BranchConnection(originBranchModel, destinationBranchModel),
                new BranchConnection(originBranchModel, destinationBranchModel2)
        );

        when(branchConnectionsRepository.findAll()).thenReturn(branchConnectionEntities);

        final var result = branchService.getAllBranchConnections();

        assertEquals(expectedBranchConnections, result);
    }

    @Test
    void testCreateBranch() {
        final var expectedBranch = new Branch("A", 5);
        final var expectedBranchEntity = new BranchEntity(1, expectedBranch.getName(), expectedBranch.getTransferCost());
        when(branchRepository.save(any(BranchEntity.class))).thenReturn(expectedBranchEntity);
        final var actualBranch = branchService.createBranch(expectedBranch);
        assertEquals(expectedBranch, actualBranch);
        verify(branchRepository).save(any(BranchEntity.class));
    }

    @Test
    void testCreateBranchWithInvalidTransferCost() {
        final var expectedBranch = new Branch("A", -5);
        final var expectedErrors = List.of(
                ErrorResponse.builder()
                        .property("$.transferCost")
                        .description("Cannot be negative")
                        .build()
        );

        thenThrownBy(() -> branchService.createBranch(expectedBranch))
                .isExactlyInstanceOf(ErrorsException.class)
                .extracting("errors")
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsExactlyElementsOf(expectedErrors);
        verify(branchRepository, never()).save(any());
    }

    @Test
    void testCreateBranchWithNullProps() {
        final var branch = new Branch(null, null);
        final var expectedErrors = List.of(
                ErrorResponse.builder()
                        .property("$.name")
                        .description("Property is blank")
                        .build(),
                ErrorResponse.builder()
                        .property("$.transferCost")
                        .description("Property is blank")
                        .build()
        );

        thenThrownBy(() -> branchService.createBranch(branch))
                .isExactlyInstanceOf(ErrorsException.class)
                .extracting("errors")
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsExactlyElementsOf(expectedErrors);
        verify(branchRepository, never()).save(any());
    }

    @Test
    void testCreateBranchWhichAlreadyExists() {
        final var branch = new Branch("A", 5);
        final var expectedErrors = List.of(
                ErrorResponse.builder()
                        .property("$.name")
                        .description("Already exists")
                        .build()
        );

        when(branchRepository.existsByName(branch.getName())).thenReturn(true);

        thenThrownBy(() -> branchService.createBranch(branch))
                .isExactlyInstanceOf(ErrorsException.class)
                .extracting("errors")
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsExactlyElementsOf(expectedErrors);
        verify(branchRepository, never()).save(any());
    }


    @Test
    void testCreateBranchConnection() {
        final var branchEntityA = new BranchEntity(1, "A", 5);
        final var branchEntityB = new BranchEntity(2, "B", 10);
        final var expectedBranchConnection = new BranchConnection(
                new Branch(branchEntityA.getName(), branchEntityA.getTransferCost()),
                new Branch(branchEntityB.getName(), branchEntityB.getTransferCost())
        );
        final var createRequest = new BranchConnectionRequest(branchEntityA.getName(), branchEntityB.getName());
        final var expectedBranchConnectionEntity = new BranchConnectionEntity(1, branchEntityA, branchEntityB);

        when(branchRepository.existsByName(branchEntityA.getName())).thenReturn(true);
        when(branchRepository.existsByName(branchEntityB.getName())).thenReturn(true);
        when(branchRepository.findByName(branchEntityA.getName())).thenReturn(Optional.of(branchEntityA));
        when(branchRepository.findByName(branchEntityB.getName())).thenReturn(Optional.of(branchEntityB));
        when(branchConnectionsRepository.save(any(BranchConnectionEntity.class))).thenReturn(expectedBranchConnectionEntity);

        final var actualBranchConnection = branchService.createBranchConnection(createRequest);

        assertEquals(expectedBranchConnection, actualBranchConnection);
        verify(branchConnectionsRepository).save(any(BranchConnectionEntity.class));
    }

    @Test
    void testCreateBranchConnectionWithNonExistentBranches() {
        final var originBranch = "A";
        final var destinationBranch = "B";
        final var createRequest = new BranchConnectionRequest(originBranch, destinationBranch);
        final var expectedErrors = List.of(
                ErrorResponse.builder()
                        .property("$.originBranch")
                        .description("Does not exist")
                        .build(),
                ErrorResponse.builder()
                        .property("$.destinationBranch")
                        .description("Does not exist")
                        .build()
        );

        when(branchRepository.existsByName(originBranch)).thenReturn(false);
        when(branchRepository.existsByName(destinationBranch)).thenReturn(false);


        thenThrownBy(() -> branchService.createBranchConnection(createRequest))
                .isExactlyInstanceOf(ErrorsException.class)
                .extracting("errors")
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsExactlyElementsOf(expectedErrors);
        verify(branchConnectionsRepository, never()).save(any());
    }

    @Test
    void testCreateBranchConnectionWithNullProps() {
        final String originBranch = null;
        final String destinationBranch = null;
        final var createRequest = new BranchConnectionRequest(originBranch, destinationBranch);
        final var expectedErrors = List.of(
                ErrorResponse.builder()
                        .property("$.originBranch")
                        .description("Property is blank")
                        .build(),
                ErrorResponse.builder()
                        .property("$.destinationBranch")
                        .description("Property is blank")
                        .build()
        );

        thenThrownBy(() -> branchService.createBranchConnection(createRequest))
                .isExactlyInstanceOf(ErrorsException.class)
                .extracting("errors")
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsExactlyElementsOf(expectedErrors);
        verify(branchConnectionsRepository, never()).save(any());
        verify(branchRepository, never()).existsByName(any());
    }

    @Test
    void createBranchConnection_shouldThrowException_whenOriginBranchDoesNotExist() {
        final var request = new BranchConnectionRequest("A", "B");
        when(branchRepository.findByName(request.getOriginBranch())).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> branchService.createBranchConnection(request));
    }



}