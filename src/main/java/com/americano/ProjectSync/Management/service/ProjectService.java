package com.americano.ProjectSync.Management.service;

import com.americano.ProjectSync.Management.model.Project;
import com.americano.ProjectSync.Management.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project createProject(Project project){
        return projectRepository.save(project);
    }


}
