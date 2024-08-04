package com.payments.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import com.payments.entity.BranchEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("com")
class BranchRepositoryTest {

    @Autowired BranchRepository branchRepository;

    @AfterEach
    void tearDown() {
        branchRepository.deleteAll();
    }

    @Test
    void testBranchExistsByName() {
        final var branchEntity = new BranchEntity(1, "5", 5);
        branchRepository.save(branchEntity);

        boolean result = branchRepository.existsByName(branchEntity.getName());

        assertThat(result).isTrue();
    }

    @Test
    void testBranchDoesNotExistsByName() {
        final var branchEntity = new BranchEntity(1, "5", 5);

        boolean result = branchRepository.existsByName(branchEntity.getName());

        assertThat(result).isFalse();
    }

    @Test
    void testFindBranchByName() {
        final var branchEntity = new BranchEntity(null, "5", 5);
        branchRepository.save(branchEntity);

        final var result = branchRepository.findByName(branchEntity.getName());

        assertEquals(result.get(), branchEntity);
    }
}
