package edu.bsu.polaris.solver.planner.domain;



import edu.bsu.polaris.solver.common.domain.AbstractPersistable;

import java.util.List;

public class Job extends AbstractPersistable {

    private Project project;
    private JobType jobType;
    private List<ExecutionMode> executionModeList;

    private List<Job> successorJobList;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public List<ExecutionMode> getExecutionModeList() {
        return executionModeList;
    }

    public void setExecutionModeList(List<ExecutionMode> executionModeList) {
        this.executionModeList = executionModeList;
    }

    public List<Job> getSuccessorJobList() {
        return successorJobList;
    }

    public void setSuccessorJobList(List<Job> successorJobList) {
        this.successorJobList = successorJobList;
    }

}
