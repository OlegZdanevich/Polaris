package edu.bsu.polaris.solver.planner.domain.solver;

import java.io.Serializable;
import java.util.Comparator;

public class DelayStrengthComparator implements Comparator<Integer>, Serializable {

    @Override
    public int compare(Integer a, Integer b) {
        return a.compareTo(b);
    }

}
