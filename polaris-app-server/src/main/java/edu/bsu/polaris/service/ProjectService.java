package edu.bsu.polaris.service;

import edu.bsu.polaris.exception.BadRequestException;
import edu.bsu.polaris.exception.ResourceNotFoundException;
import edu.bsu.polaris.model.Project;
import edu.bsu.polaris.model.User;
import edu.bsu.polaris.payload.PagedResponse;
import edu.bsu.polaris.payload.ProjectRequest;
import edu.bsu.polaris.payload.ProjectResponse;
import edu.bsu.polaris.repository.ProjectRepository;
import edu.bsu.polaris.repository.UserRepository;
import edu.bsu.polaris.util.AppConstants;
import edu.bsu.polaris.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;


    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    public Project createProject(ProjectRequest pollRequest) {
        Project poll = new Project();
        poll.setName(pollRequest.getProject());
        return projectRepository.save(poll);
    }

    public PagedResponse<ProjectResponse> getProjectCreatedBy(String username, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Project> projects = projectRepository.findByCreatedBy(user.getId(), pageable);

        if (projects.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), projects.getNumber(),
                    projects.getSize(), projects.getTotalElements(), projects.getTotalPages(), projects.isLast());
        }

        List<ProjectResponse> projectResponses = projects.map(project -> {
            return ModelMapper.mapProjectToProjectResponse(project, user);
        }).getContent();

        return new PagedResponse<>(projectResponses, projects.getNumber(),
                projects.getSize(), projects.getTotalElements(), projects.getTotalPages(), projects.isLast());
    }

    public ProjectResponse getProjectById(long id){
        Project project=projectRepository.findById(id).orElse(new Project());
        return ModelMapper.mapProjectToProjectResponse(project);
    }





    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

}
