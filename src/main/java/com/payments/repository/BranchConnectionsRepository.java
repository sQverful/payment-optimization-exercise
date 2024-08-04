package com.payments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.payments.entity.BranchConnectionEntity;

@Repository
public interface BranchConnectionsRepository extends JpaRepository<BranchConnectionEntity, Integer> {

}
