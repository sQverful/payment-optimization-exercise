package com.payments.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "branch_connection")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchConnectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "origin_branch_id", nullable = false)
    private BranchEntity originBranch;

    @ManyToOne
    @JoinColumn(name = "destination_branch_id", nullable = false)
    private BranchEntity destinationBranch;
}

