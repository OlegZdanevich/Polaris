package edu.bsu.polaris.payload;

import lombok.Data;

@Data
public class AllocationResponse {
    private long jobId;
    private int start;
    private int end;
}
