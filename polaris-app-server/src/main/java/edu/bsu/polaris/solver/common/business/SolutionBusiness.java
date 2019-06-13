package edu.bsu.polaris.solver.common.business;

import edu.bsu.polaris.solver.common.persistence.AbstractTxtSolutionImporter;
import edu.bsu.polaris.solver.planner.domain.Schedule;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.score.constraint.Indictment;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.score.director.ScoreDirectorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SolutionBusiness {
    protected final transient Logger logger = LoggerFactory.getLogger(getClass());
    private File dataDir;
    private AbstractTxtSolutionImporter<Schedule> importer;
    private File importDataDir;
    private volatile Solver<Schedule> solver;
    private String solutionFileName = null;
    private ScoreDirector<Schedule> guiScoreDirector;

    public void setDataDir(File dataDir) {
        this.dataDir = dataDir;
    }


    public void setImporters(AbstractTxtSolutionImporter<Schedule> importer) {
        this.importer = importer;
    }

    public void updateDataDirs() {

        importDataDir = dataDir;
        if (!importDataDir.exists()) {
            throw new IllegalStateException("The directory importDataDir (" + importDataDir.getAbsolutePath()
                    + ") does not exist.");
        }

    }

    public File getImportDataDir() {
        return importDataDir;
    }


    public void setSolver(Solver<Schedule> solver) {
        this.solver = solver;
        ScoreDirectorFactory<Schedule> scoreDirectorFactory = solver.getScoreDirectorFactory();
        guiScoreDirector = scoreDirectorFactory.buildScoreDirector();
    }


    public Schedule getSolution() {
        return guiScoreDirector.getWorkingSolution();
    }

    public void setSolution(Schedule solution) {
        guiScoreDirector.setWorkingSolution(solution);
    }

    public String getSolutionFileName() {
        return solutionFileName;
    }

    public Score getScore() {
        return guiScoreDirector.calculateScore();
    }

    public boolean isSolving() {
        return solver.isSolving();
    }


    public boolean isConstraintMatchEnabled() {
        return guiScoreDirector.isConstraintMatchEnabled();
    }

    public List<ConstraintMatchTotal> getConstraintMatchTotalList() {
        List<ConstraintMatchTotal> constraintMatchTotalList = new ArrayList<>(
                guiScoreDirector.getConstraintMatchTotals());
        Collections.sort(constraintMatchTotalList);
        return constraintMatchTotalList;
    }

    public Map<Object, Indictment> getIndictmentMap() {
        return guiScoreDirector.getIndictmentMap();
    }

    public void importSolution(File file) {
        Schedule solution = importer.readSolution(file);
        solutionFileName = file.getName();
        guiScoreDirector.setWorkingSolution(solution);
    }



    public void doMove(Move<Schedule> move) {
        if (solver.isSolving()) {
            logger.error("Not doing user move ({}) because the solver is solving.", move);
            return;
        }
        if (!move.isMoveDoable(guiScoreDirector)) {
            logger.warn("Not doing user move ({}) because it is not doable.", move);
            return;
        }
        logger.info("Doing user move ({}).", move);
        move.doMove(guiScoreDirector);
        guiScoreDirector.calculateScore();
    }

    public Schedule solve(Schedule problem) {
        return solver.solve(problem);
    }

    public void terminateSolvingEarly() {
        solver.terminateEarly();
    }
}
