package edu.bsu.polaris.payload;

import lombok.Data;

import java.util.List;

@Data
public class ProjectAllocationResponse {
    private long scheduleId;
    private List<AllocationsByFlowResponse> flows;
}
