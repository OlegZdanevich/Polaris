package edu.bsu.polaris.controller;


import edu.bsu.polaris.model.Project;
import edu.bsu.polaris.payload.*;
import edu.bsu.polaris.service.ProjectService;
import edu.bsu.polaris.solver.planner.app.ProjectJobSchedulingService;
import edu.bsu.polaris.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;


@RestController
@RequestMapping("/api/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectJobSchedulingService projectJobSchedulingService;

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectRequest projectRequest) {
        System.out.println(projectRequest);
        Project poll = projectService.createProject(projectRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(poll.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Poll Created Successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ProjectAllocationResponse getAllocations(@RequestParam(value = "project_id")long id){
       return projectJobSchedulingService.getStartAllocation( projectService.getProjectById(id).getFileName());
    }

    @GetMapping("/users/{username}/project")
    @PreAuthorize("hasRole('USER')")
    public PagedResponse<ProjectResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
                                                            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return projectService.getProjectCreatedBy(username, page, size);
    }



    @GetMapping("/count/time")
    @PreAuthorize("hasRole('USER')")
    public ProjectAllocationResponse countProject(@RequestParam(value = "project_id")long id,@RequestParam(value = "best_Solution")long minutes){
        return projectJobSchedulingService.getFinalAllocation( projectService.getProjectById(id).getFileName(),minutes);
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('USER')")
    public ProjectAllocationResponse countProjectBest(@RequestParam(value = "project_id")long id){
        return projectJobSchedulingService.getFinalAllocation( projectService.getProjectById(id).getFileName());
    }




}
