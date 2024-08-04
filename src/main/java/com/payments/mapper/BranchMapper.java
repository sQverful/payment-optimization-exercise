package com.payments.mapper;

import org.mapstruct.Mapper;
import com.payments.entity.BranchEntity;
import com.payments.model.Branch;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    Branch toBranch(BranchEntity branchEntity);

    BranchEntity toBranchEntity(Branch branch);

    List<Branch> toBranches(List<BranchEntity> branchEntities);

    List<BranchEntity> toBranchEntities(List<Branch> branches);
}

