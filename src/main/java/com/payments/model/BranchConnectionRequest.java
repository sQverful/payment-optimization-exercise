package com.payments.model;

import lombok.Data;

@Data
public class BranchConnectionRequest {
    private final String originBranch;
    private final String destinationBranch;
}
