package edu.bsu.polaris.solver.planner.app;

import edu.bsu.polaris.payload.AllocationResponse;
import edu.bsu.polaris.payload.AllocationsByFlowResponse;
import edu.bsu.polaris.payload.ProjectAllocationResponse;
import edu.bsu.polaris.solver.common.business.SolutionBusiness;
import edu.bsu.polaris.solver.planner.domain.Allocation;
import edu.bsu.polaris.solver.planner.domain.Project;
import edu.bsu.polaris.solver.planner.domain.Schedule;
import edu.bsu.polaris.solver.planner.persistence.ProjectJobSchedulingImporter;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ProjectJobSchedulingService {

    private static final String SOLVER_CONFIG_HARD_SCORE
            = "solver/solverConfigHard.xml";

    private static final String SOLVER_CONFIG_BEST
            = "solver/solverConfigBest.xml";

    private static final String DATA_DIR_SYSTEM_PROPERTY = "edu.bsu";


    private static final String DATA_DIR_NAME = "data";
    private final static Logger logger = LoggerFactory.getLogger(ProjectJobSchedulingApp.class);

    public ProjectAllocationResponse getStartAllocation(String filename) {
        SolutionBusiness solutionBusiness = createSolutionBusiness();
        solutionBusiness.importSolution(new File("polaris-app-server/src/main/resources/data/" + filename));
        Schedule problemStart = solutionBusiness.getSolution();
        return getProjectAllocationResponse(problemStart);
    }

    public ProjectAllocationResponse getFinalAllocation(String filename) {
        SolutionBusiness solutionBusiness = createSolutionBusiness();
        solutionBusiness.importSolution(new File("polaris-app-server/src/main/resources/data/"+filename));
        Schedule problemStart = solutionBusiness.getSolution();
        Schedule problemFinish = solutionBusiness.solve(problemStart);
        return getProjectAllocationResponse(problemFinish);
    }

    public ProjectAllocationResponse getFinalAllocation(String filename,long time) {
        SolutionBusiness solutionBusiness = createSolutionBusiness(time);
        solutionBusiness.importSolution(new File("polaris-app-server/src/main/resources/data/"+filename));
        Schedule problemStart = solutionBusiness.getSolution();
        Schedule problemFinish = solutionBusiness.solve(problemStart);
        return getProjectAllocationResponse(problemFinish);
    }
    private static ProjectAllocationResponse getProjectAllocationResponse(Schedule problemStart) {
        List<Long> projectIds = problemStart.getProjectList().stream().filter(distinctByKey(Project::getId)).map(e -> e.getId()).collect(Collectors.toList());
        List<Allocation> allocations = problemStart.getAllocationList();
        ProjectAllocationResponse projectAllocationResponse = new ProjectAllocationResponse();
        List<AllocationsByFlowResponse> allocationsByFlows = new ArrayList<>();
        for (long id : projectIds) {
            AllocationsByFlowResponse allocationsByFlow = new AllocationsByFlowResponse();
            allocationsByFlow.setFlowId(id);
            List<AllocationResponse> allocationsInFlow = new ArrayList<>();
            for (Allocation allocation : allocations) {
                if (allocation.getProject().getId() == id) {
                    AllocationResponse allocationInFlow = new AllocationResponse();
                    allocationInFlow.setJobId(allocation.getJob().getId());
                    allocationInFlow.setStart(allocation.getStartDate());
                    allocationInFlow.setEnd(allocation.getEndDate());
                    allocationsInFlow.add(allocationInFlow);
                }
            }
            allocationsByFlow.setAllocations(allocationsInFlow);
            allocationsByFlows.add(allocationsByFlow);
        }
        projectAllocationResponse.setFlows(allocationsByFlows);
        projectAllocationResponse.setScheduleId(problemStart.getId());
        return projectAllocationResponse;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private SolutionBusiness createSolutionBusiness() {
        SolutionBusiness solutionBusiness = new SolutionBusiness();
        solutionBusiness.setSolver(createSolver());
        solutionBusiness.setDataDir(determineDataDir(DATA_DIR_NAME));
        solutionBusiness.setImporters(new ProjectJobSchedulingImporter());
        solutionBusiness.updateDataDirs();
        return solutionBusiness;
    }

    private SolutionBusiness createSolutionBusiness(long time) {
        SolutionBusiness solutionBusiness = new SolutionBusiness();
        solutionBusiness.setSolver(createSolver(time));
        solutionBusiness.setDataDir(determineDataDir(DATA_DIR_NAME));
        solutionBusiness.setImporters(new ProjectJobSchedulingImporter());
        solutionBusiness.updateDataDirs();
        return solutionBusiness;
    }

    private Solver<Schedule> createSolver() {
        SolverFactory<Schedule> solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG_HARD_SCORE);
        return solverFactory.buildSolver();
    }
    private Solver<Schedule> createSolver(long time) {
        SolverFactory<Schedule> solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG_BEST);
        SolverConfig config=solverFactory.getSolverConfig();
        TerminationConfig terminationConfig=new TerminationConfig();
        terminationConfig.setMinutesSpentLimit(time);
        config.setTerminationConfig(terminationConfig);
        return solverFactory.buildSolver();
    }

    private File determineDataDir(String dataDirName) {
        String dataDirPath = System.getProperty(DATA_DIR_SYSTEM_PROPERTY, "polaris-app-server/src/main/resources");
        File dataDir = new File(dataDirPath, dataDirName);
        if (!dataDir.exists()) {
            logger.error("The directory dataDir (" + dataDir.getAbsolutePath()
                    + ") does not exist");
            throw new IllegalStateException("The directory dataDir (" + dataDir.getAbsolutePath()
                    + ") does not exist");
        }
        return dataDir;
    }
}
