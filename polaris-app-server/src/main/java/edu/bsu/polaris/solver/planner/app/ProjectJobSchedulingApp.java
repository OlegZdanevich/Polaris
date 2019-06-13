package edu.bsu.polaris.solver.planner.app;

import edu.bsu.polaris.solver.common.business.SolutionBusiness;
import edu.bsu.polaris.solver.planner.domain.Allocation;
import edu.bsu.polaris.solver.planner.domain.Schedule;
import edu.bsu.polaris.solver.planner.persistence.ProjectJobSchedulingImporter;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class ProjectJobSchedulingApp {

    private static final String SOLVER_CONFIG
            = "solver/solverConfigBest.xml";

    private static final String DATA_DIR_SYSTEM_PROPERTY = "org.optaplanner.examples.dataDir";

    private static SolutionBusiness solutionBusiness;

    private static final String DATA_DIR_NAME = "data";
    private final static Logger logger = LoggerFactory.getLogger(ProjectJobSchedulingApp.class);



    public static void main(String[] args) {
        solutionBusiness = createSolutionBusiness();
        solutionBusiness.importSolution(new File("polaris-app-server/src/main/resources/data/A-10.txt"));
        Schedule problemStart = solutionBusiness.getSolution();
        Schedule problemFinish = solutionBusiness.solve(problemStart);
        printSolution(problemFinish);
    }

    private static SolutionBusiness createSolutionBusiness() {
        SolutionBusiness solutionBusiness = new SolutionBusiness();
        solutionBusiness.setSolver(createSolver());
        solutionBusiness.setDataDir(determineDataDir(DATA_DIR_NAME));
        solutionBusiness.setImporters( new ProjectJobSchedulingImporter());
        solutionBusiness.updateDataDirs();
        return solutionBusiness;
    }

    private static Solver<Schedule> createSolver() {
        SolverFactory<Schedule> solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);
        return solverFactory.buildSolver();
    }

    private static File determineDataDir(String dataDirName) {
        String dataDirPath = System.getProperty(DATA_DIR_SYSTEM_PROPERTY, "polaris-app-server/src/main/resources");
        File dataDir = new File(dataDirPath, dataDirName);
        if (!dataDir.exists()) {
            throw new IllegalStateException("The directory dataDir (" + dataDir.getAbsolutePath()
                    + ") does not exist.\n" +
                    " Either the working directory should be set to the directory that contains the data directory" +
                    " (which is not the data directory itself), or the system property "
                    + DATA_DIR_SYSTEM_PROPERTY + " should be set properly.\n" +
                    " The data directory is different in a git clone (optaplanner/optaplanner-examples/data)" +
                    " and in a release zip (examples/sources/data).\n" +
                    " In an IDE (IntelliJ, Eclipse, NetBeans), open the \"Run configuration\""
                    + " to change \"Working directory\" (or add the system property in \"VM options\").");
        }
        return dataDir;
    }

    private static void printSolution(Schedule schedule){
        for (Allocation allocation:schedule.getAllocationList()){
            printJob(allocation);
        }
    }

    private static void printJob(Allocation allocation){
        logger.info("id: "+allocation.getJob().getId()+"| start: "+allocation.getStartDate()+ " end: "+allocation.getEndDate());
    }

}
