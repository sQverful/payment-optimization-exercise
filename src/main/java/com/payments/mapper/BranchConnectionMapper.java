package com.payments.mapper;


import org.mapstruct.Mapper;
import com.payments.entity.BranchConnectionEntity;
import com.payments.model.BranchConnection;

import java.util.List;

@Mapper(uses = BranchMapper.class, componentModel = "spring")
public interface BranchConnectionMapper {
    BranchConnectionEntity toBranchConnectionEntity(BranchConnection branchConnection);

    BranchConnection toBranchConnection(BranchConnectionEntity branchConnectionEntity);

    List<BranchConnection> toBranchConnections(List<BranchConnectionEntity> branchConnectionEntities);

    List<BranchConnectionEntity> toBranchConnectionEntities(List<BranchConnection> branchConnections);
}
