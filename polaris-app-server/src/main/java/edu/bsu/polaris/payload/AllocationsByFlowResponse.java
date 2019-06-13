package edu.bsu.polaris.payload;

import lombok.Data;

import java.util.List;

@Data
public class AllocationsByFlowResponse {
    private long flowId;
    private List<AllocationResponse> allocations;
}
