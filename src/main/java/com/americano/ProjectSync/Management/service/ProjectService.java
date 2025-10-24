package com.americano.ProjectSync.Management.service;

import com.americano.ProjectSync.Management.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.americano.ProjectSync.Management.exception.ResourcesNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Logic for CRUD operations DELETE by ID
    @Transactional
    public void deleteProject(Long id){
        // We check if the project exists before deleting it
        boolean exists = projectRepository.existsById(id);
        // If the project does not exist, it enters the conditional to launch the NotFound
        if(!exists){
            throw new ResourcesNotFoundException("Project with id " + id + " does not exists");
        }
        // The delete operation continues and the project is deleted
        projectRepository.deleteById(id);
    }
}
