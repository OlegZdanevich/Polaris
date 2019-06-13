

package edu.bsu.polaris.solver.planner.solver.score.capacity;


import edu.bsu.polaris.solver.planner.domain.Allocation;
import edu.bsu.polaris.solver.planner.domain.ResourceRequirement;
import edu.bsu.polaris.solver.planner.domain.resource.Resource;

public abstract class ResourceCapacityTracker {

    protected Resource resource;

    public ResourceCapacityTracker(Resource resource) {
        this.resource = resource;
    }

    public abstract void insert(ResourceRequirement resourceRequirement, Allocation allocation);

    public abstract void retract(ResourceRequirement resourceRequirement, Allocation allocation);

    public abstract int getHardScore();

}
