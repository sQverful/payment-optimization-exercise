package com.payments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.payments.model.Branch;
import com.payments.model.BranchConnection;
import com.payments.model.BranchConnectionRequest;
import com.payments.service.BranchService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("api/v1/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @GetMapping
    public ResponseEntity<List<Branch>> getAllBranches() {
        final var branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }

    @PostMapping
    public ResponseEntity<Branch> createBranch(@RequestBody Branch branch) {
        final var createdBranch = branchService.createBranch(branch);
        return new ResponseEntity<>(createdBranch, CREATED);
    }

    @GetMapping("/connections")
    public ResponseEntity<List<BranchConnection>> getAllBranchConnections() {
        final var branchConnections = branchService.getAllBranchConnections();
        return ResponseEntity.ok(branchConnections);
    }

    @PostMapping("/connections")
    public ResponseEntity<BranchConnection> createBranchConnection(@RequestBody BranchConnectionRequest request) {
        final var createdBranch = branchService.createBranchConnection(request);
        return new ResponseEntity<>(createdBranch, CREATED);
    }

    //TODO: add support for delete,update operations

}
