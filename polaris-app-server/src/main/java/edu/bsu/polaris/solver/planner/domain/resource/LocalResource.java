package edu.bsu.polaris.solver.planner.domain.resource;


import edu.bsu.polaris.solver.planner.domain.Project;

public class LocalResource extends Resource {

    private Project project;
    private boolean renewable;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean isRenewable() {
        return renewable;
    }

    public void setRenewable(boolean renewable) {
        this.renewable = renewable;
    }
}
