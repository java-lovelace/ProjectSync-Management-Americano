package com.americano.ProjectSync.Management.service;

import com.americano.ProjectSync.Management.exception.ResourcesNotFoundException;
import com.americano.ProjectSync.Management.model.Project;
import com.americano.ProjectSync.Management.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.americano.ProjectSync.Management.exception.ResourcesNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("Project not found with id: " + id));
    }

    public Project updateProject(Long id, Project projectDetails) {
        Project existingProject = getProjectById(id);

        // Título (Requerido: no nulo ni en blanco)
        safeUpdate(projectDetails.getTitle(), existingProject::setTitle, true);

        // Descripción (No Requerido: puede ser nulo, pero no necesita ser validado contra isBlank)
        safeUpdate(projectDetails.getDescription(), existingProject::setDescription, false);

        // Status (Requerido: no nulo ni en blanco)
        safeUpdate(projectDetails.getStatus(), existingProject::setStatus, true);

        // Persona Responsable (Requerido: no nulo ni en blanco)
        safeUpdate(projectDetails.getResponsiblePerson(), existingProject::setResponsiblePerson, true);

        return projectRepository.save(existingProject);
    }

    private void safeUpdate(String newValue, java.util.function.Consumer<String> setter, boolean checkBlank) {
        if (newValue != null) {
            if (checkBlank && newValue.isBlank()) {
                // Si se espera un valor (checkBlank=true) y viene vacío,
                return;
            }
            setter.accept(newValue);
        }
    }
 
    // Logic for CRUD operations DELETE by ID
    @Transactional
    public void deleteProject(Long id) {
        // We check if the project exists before deleting it
        boolean exists = projectRepository.existsById(id);
        // If the project does not exist, it enters the conditional to launch the NotFound
        if (!exists) {
            throw new ResourcesNotFoundException("Project with id " + id + " does not exists");
        }
        // The delete operation continues and the project is deleted
        projectRepository.deleteById(id);
    }
}
