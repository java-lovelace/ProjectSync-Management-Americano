package com.americano.ProjectSync.Management.service;

import com.americano.ProjectSync.Management.exception.ResourcesNotFoundException;
import com.americano.ProjectSync.Management.model.Project;
import com.americano.ProjectSync.Management.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {


    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project mockProject;
    private final Long PROJECT_ID = 1L;

    @BeforeEach
    void setUp() {
        // Inicializa un proyecto "existente" para simular que se recupera de la DB
        mockProject = new Project("Original Title", "Original Description", "Active", "Original Responsible");
        mockProject.setId(PROJECT_ID);
    }

    // -------------------------------------------------------------------------
    // Pruebas para UPDATE
    // -------------------------------------------------------------------------

    @Test
    void testUpdateProject_Success_AllFieldsUpdated() {
        // Arrange: Objeto con todos los nuevos detalles
        Project projectDetails = new Project("Updated Title", "New Description", "Completed", "New Responsible");

        // Simular: Cuando findById es llamado, devuelve el proyecto existente
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(mockProject));

        // Simular: Cuando save es llamado, devuelve el objeto que se le pasó (el mockProject modificado)
        // Esto asume que tu método service usa save(existingProject)
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Project updatedProject = projectService.updateProject(PROJECT_ID, projectDetails);

        // Assert
        assertNotNull(updatedProject);

        // Verificar que los campos fueron actualizados
        assertEquals(PROJECT_ID, updatedProject.getId(), "El ID debe permanecer sin cambios.");
        assertEquals("Updated Title", updatedProject.getTitle());
        assertEquals("Completed", updatedProject.getStatus());
        assertEquals("New Responsible", updatedProject.getResponsiblePerson());

        // Verificar que los métodos findById y save fueron llamados exactamente una vez
        verify(projectRepository, times(1)).findById(PROJECT_ID);
        verify(projectRepository, times(1)).save(mockProject); // Verifica que se guardó el objeto original (modificado)
    }

    @Test
    void testUpdateProject_Success_PartialUpdate() {
        // Arrange: Objeto con solo el título para actualizar (los otros campos vienen nulos/vacíos)
        Project partialDetails = new Project();
        partialDetails.setTitle("Partial Title Change");

        // Asumiendo que tu método service (Opción 2) ignora los nulos,
        // los valores originales deben persistir.

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(mockProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Project updatedProject = projectService.updateProject(PROJECT_ID, partialDetails);

        // Assert
        // El título debe cambiar
        assertEquals("Partial Title Change", updatedProject.getTitle());

        // Los campos no enviados (nulos) deben mantener el valor original
        // gracias a la lógica de comprobación de nulos en el Service.
        assertEquals("Active", updatedProject.getStatus(), "El status debe mantener el valor original.");
        assertEquals("Original Responsible", updatedProject.getResponsiblePerson(), "El responsable debe mantener el valor original.");

        verify(projectRepository, times(1)).save(mockProject);
    }

    @Test
    void testUpdateProject_NotFound_ThrowsException() {
        // Arrange
        Project projectDetails = new Project("Title", "Desc", "Status", "Resp");
        Long nonExistentId = 99L;

        // Simular: Cuando findById es llamado, devuelve Optional vacío
        when(projectRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Assert: Espera que se lance ResourcesNotFoundException
        assertThrows(ResourcesNotFoundException.class, () -> {
            projectService.updateProject(nonExistentId, projectDetails);
        });

        // Verificar que NO se intentó guardar nada
        verify(projectRepository, times(1)).findById(nonExistentId);
        verify(projectRepository, times(0)).save(any(Project.class));

    }

    @Test
    void testDeleteProject_Success() {
        // 1. Arrange (Preparar)
        Long projectId = 1L;
        // Configuramos el mock: cuando se llame a existsById(1L), debe devolver true
        when(projectRepository.existsById(projectId)).thenReturn(true);
        // Configuramos el mock: deleteById no devuelve nada (void), así que usamos doNothing()
        doNothing().when(projectRepository).deleteById(projectId);

        // 2. Act (Actuar)
        projectService.deleteProject(projectId);

        // 3. Assert (Verificar)
        // Verificamos que existsById fue llamado 1 vez
        verify(projectRepository, times(1)).existsById(projectId);
        // Verificamos que deleteById fue llamado 1 vez
        verify(projectRepository, times(1)).deleteById(projectId);
    }

    @Test
    void testDeleteProject_NotFound() {
        // 1. Arrange (Preparar)
        Long projectId = 99L;
        // Configuramos el mock: cuando se llame a existsById(99L), debe devolver false
        when(projectRepository.existsById(projectId)).thenReturn(false);

        // 2. Act & Assert (Actuar y Verificar)
        // Verificamos que al llamar a deleteProject(99L), se lanza la excepción correcta
        assertThrows(ResourcesNotFoundException.class, () -> {
            projectService.deleteProject(projectId);
        });

        // Verificamos que deleteById NUNCA fue llamado, porque la excepción se lanzó antes
        verify(projectRepository, never()).deleteById(anyLong());
    }
}