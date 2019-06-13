package edu.bsu.polaris.solver.planner.solver.score.drools;

import edu.bsu.polaris.solver.planner.domain.resource.Resource;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


import java.io.Serializable;

public class RenewableResourceUsedDay implements Serializable {

    private final Resource resource;
    private final int usedDay;

    public RenewableResourceUsedDay(Resource resource, int usedDay) {
        this.resource = resource;
        this.usedDay = usedDay;
    }

    public Resource getResource() {
        return resource;
    }

    public int getUsedDay() {
        return usedDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof RenewableResourceUsedDay) {
            RenewableResourceUsedDay other = (RenewableResourceUsedDay) o;
            return new EqualsBuilder()
                    .append(resource, other.resource)
                    .append(usedDay, other.usedDay)
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(resource)
                .append(usedDay)
                .toHashCode();
    }

    @Override
    public String toString() {
        return resource + " on " + usedDay;
    }

    public int getResourceCapacity() {
        return resource.getCapacity();
    }

}
