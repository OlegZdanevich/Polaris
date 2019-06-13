package edu.bsu.polaris.solver.planner.domain;


import edu.bsu.polaris.solver.common.domain.AbstractPersistable;
import edu.bsu.polaris.solver.planner.domain.resource.Resource;

public class ResourceRequirement extends AbstractPersistable {

    private ExecutionMode executionMode;
    private Resource resource;
    private int requirement;

    public ExecutionMode getExecutionMode() {
        return executionMode;
    }

    public void setExecutionMode(ExecutionMode executionMode) {
        this.executionMode = executionMode;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public int getRequirement() {
        return requirement;
    }

    public void setRequirement(int requirement) {
        this.requirement = requirement;
    }

    public boolean isResourceRenewable() {
        return resource.isRenewable();
    }

}
