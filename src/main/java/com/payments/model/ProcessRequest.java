package com.payments.model;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class ProcessRequest {
    private String originBranch;
    private String destinationBranch;
}
