package edu.bsu.polaris.solver.planner.domain.solver;

import edu.bsu.polaris.solver.planner.domain.Allocation;
import edu.bsu.polaris.solver.planner.domain.JobType;
import edu.bsu.polaris.solver.planner.domain.Schedule;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.score.director.ScoreDirector;


public class NotSourceOrSinkAllocationFilter implements SelectionFilter<Schedule, Allocation> {

    @Override
    public boolean accept(ScoreDirector<Schedule> scoreDirector, Allocation allocation) {
        JobType jobType = allocation.getJob().getJobType();
        return jobType != JobType.SOURCE && jobType != JobType.SINK;
    }

}
