package com.americano.ProjectSync.Management.service;

import com.americano.ProjectSync.Management.exception.ResourcesNotFoundException;
import com.americano.ProjectSync.Management.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

// Usamos MockitoExtension para habilitar las anotaciones @Mock e @InjectMocks
@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    // @Mock crea una simulación (un "doble") del repositorio.
    // No usará la base de datos real.
    @Mock
    private ProjectRepository projectRepository;

    // @InjectMocks crea una instancia de ProjectService e inyecta
    // los mocks (como projectRepository) en él.
    @InjectMocks
    private ProjectService projectService;

    // --- Tests para tu Tarea 4 ---

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