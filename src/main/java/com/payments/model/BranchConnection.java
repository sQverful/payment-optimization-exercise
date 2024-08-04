package com.payments.model;

import lombok.Data;
@Data
public class BranchConnection {
    private final Branch originBranch;
    private final Branch destinationBranch;

    @Override
    public String toString() {
        return "BranchConnection{" +
                "source=" + originBranch.getName() +
                ", destination=" + destinationBranch.getName() +
                '}';
    }
}
