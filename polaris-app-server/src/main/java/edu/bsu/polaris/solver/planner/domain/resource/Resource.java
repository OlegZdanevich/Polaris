package edu.bsu.polaris.solver.planner.domain.resource;


import edu.bsu.polaris.solver.common.domain.AbstractPersistable;

public abstract class Resource extends AbstractPersistable {

    private int capacity;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public abstract boolean isRenewable();

}
