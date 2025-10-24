package com.americano.ProjectSync.Management.service;

import com.americano.ProjectSync.Management.model.Project;
import com.americano.ProjectSync.Management.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;
    @Test
    void whenCreateProject_shouldReturnSavedProject() {

        Project projectToSave = new Project();
        projectToSave.setTitle("Nuevo Proyecto");
        projectToSave.setStatus("Activo");
        projectToSave.setResponsiblePerson("Usuario de Prueba");

        Project savedProject = new Project();
        savedProject.setId(1L);
        savedProject.setTitle("Nuevo Proyecto");
        savedProject.setStatus("Activo");
        savedProject.setResponsiblePerson("Usuario de Prueba");

        given(projectRepository.save(any(Project.class))).willReturn(savedProject);
        Project result = projectService.createProject(projectToSave);
        assertNotNull(result);

        assertEquals(1L, result.getId());

        assertEquals("Nuevo Proyecto", result.getTitle());
    }
}