package edu.bsu.polaris.util;

import edu.bsu.polaris.model.Project;
import edu.bsu.polaris.model.User;
import edu.bsu.polaris.payload.ProjectResponse;
import edu.bsu.polaris.payload.UserSummary;

public class ModelMapper {
    public static ProjectResponse mapProjectToProjectResponse(Project project, User creator) {
        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setId(project.getId());
        projectResponse.setProject(project.getName());
        projectResponse.setCreationDateTime(project.getCreatedAt());
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        projectResponse.setCreatedBy(creatorSummary);
        return projectResponse;
    }

    public static ProjectResponse mapProjectToProjectResponse(Project project) {
        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setId(project.getId());
        projectResponse.setProject(project.getName());
        projectResponse.setCreationDateTime(project.getCreatedAt());
        projectResponse.setFileName(project.getFileName());
        return projectResponse;
    }


}
