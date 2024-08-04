package com.payments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.payments.entity.BranchEntity;

import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<BranchEntity, Integer> {

    @Query("SELECT b FROM BranchEntity b WHERE b.name = :name")
    Optional<BranchEntity> findByName(@Param("name") String name);

    @Query("SELECT count(b) > 0 FROM BranchEntity b WHERE b.name = :name")
    boolean existsByName(@Param("name") String name);
}
