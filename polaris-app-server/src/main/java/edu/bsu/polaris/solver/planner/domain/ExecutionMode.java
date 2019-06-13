package edu.bsu.polaris.solver.planner.domain;

import edu.bsu.polaris.solver.common.domain.AbstractPersistable;

import java.util.List;

public class ExecutionMode extends AbstractPersistable {

    private Job job;
    private int duration; // In days

    private List<ResourceRequirement> resourceRequirementList;

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<ResourceRequirement> getResourceRequirementList() {
        return resourceRequirementList;
    }

    public void setResourceRequirementList(List<ResourceRequirement> resourceRequirementList) {
        this.resourceRequirementList = resourceRequirementList;
    }

}
