package com.americano.ProjectSync.Management.controller;

import com.americano.ProjectSync.Management.exception.ResourcesNotFoundException;
import com.americano.ProjectSync.Management.model.Project;
import com.americano.ProjectSync.Management.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ProjectController.class,
        excludeAutoConfiguration = {
                HibernateJpaAutoConfiguration.class,
                JpaRepositoriesAutoConfiguration.class
        }
)
// Importa una configuración de prueba vacía para asegurar que no se carguen beans JPA no deseados.
@Import(ProjectControllerTest.TestConfig.class)
class ProjectControllerTest {
import com.americano.ProjectSync.Management.model.Project;
import com.americano.ProjectSync.Management.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // Carga el contexto completo de Spring Boot
@AutoConfigureMockMvc // Configura MockMvc para hacer peticiones HTTP
@Transactional // Asegura que cada test se ejecute en una transacción que se revierte al final
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private Project mockProject;
    private Project updatedProjectDetails;
    private final Long PROJECT_ID = 1L;
    private final String API_PATH = "/api/projects/";

    @BeforeEach
    void setUp() {
        // Proyecto original simulado
        mockProject = new Project("Original Title", "Original Description", "Active", "Original Responsible");
        mockProject.setId(PROJECT_ID);

        // Detalles para la actualización (lo que el cliente enviaría)
        updatedProjectDetails = new Project("New Title", "New Description", "Completed", "New Responsible");

        // El proyecto que el servicio devolvería después de la actualización (con el ID y los nuevos datos)
        Project fullyUpdatedProject = new Project("New Title", "New Description", "Completed", "New Responsible");
        fullyUpdatedProject.setId(PROJECT_ID);
        fullyUpdatedProject.setCreatedDate(mockProject.getCreatedDate());
        fullyUpdatedProject.setLastModifiedDate(mockProject.getLastModifiedDate());
    }

    // -------------------------------------------------------------------------
    // Prueba para el endpoint PUT /api/projects/{id}
    // -------------------------------------------------------------------------

    @Test
    void testUpdateProject_Success_Returns200() throws Exception {
        // Arrange
        // Simular el resultado de la actualización que devuelve el Service
        Project updatedProject = new Project("New Title", "New Description", "Completed", "New Responsible");
        updatedProject.setId(PROJECT_ID);

        when(projectService.updateProject(eq(PROJECT_ID), any(Project.class))).thenReturn(updatedProject);

        // Act & Assert
        mockMvc.perform(put(API_PATH + PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProjectDetails)))

                // 1. Verifica el código de estado HTTP 200 OK
                .andExpect(status().isOk())

                // 2. Verifica los campos del JSON de respuesta
                .andExpect(jsonPath("$.id").value(PROJECT_ID))
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.status").value("Completed"));

        // 3. Verifica que el método del servicio fue llamado
        verify(projectService, times(1)).updateProject(eq(PROJECT_ID), any(Project.class));
    }

    @Test
    void testUpdateProject_NotFound_Returns404() throws Exception {
        // Arrange
        Long nonExistentId = 99L;
        // Simular que el servicio lanza la excepción ResourcesNotFoundException
        doThrow(new ResourcesNotFoundException("Project not found")).when(projectService)
                .updateProject(eq(nonExistentId), any(Project.class));

        // Act & Assert
        mockMvc.perform(put(API_PATH + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProjectDetails)))

                // 1. Verifica el código de estado HTTP 404 Not Found
                .andExpect(status().isNotFound());

        // 2. Verifica que el método del servicio fue llamado
        verify(projectService, times(1)).updateProject(eq(nonExistentId), any(Project.class));
    }

    @Test
    void testUpdateProject_ValidationError_Returns400() throws Exception {
        // Arrange
        // Crear un objeto Project con un título en blanco para forzar la validación @NotBlank
        Project invalidDetails = new Project("", "Valid Description", "Active", "Responsible");

        // Act & Assert
        mockMvc.perform(put(API_PATH + PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDetails)))

                // 1. Verifica el código de estado HTTP 400 Bad Request
                .andExpect(status().isBadRequest());

        // 2. Verifica que el método del servicio NO fue llamado, ya que la validación falla antes.
        verify(projectService, times(0)).updateProject(any(), any());
    }

    @Configuration
    static class TestConfig {
        // No se necesitan beans. Su presencia en el @Import es suficiente
        // para un contexto de prueba limpio.
    @Autowired
    private ProjectRepository projectRepository;

    private Project testProject;

    @BeforeEach
    void setUp() {
        // Limpiamos la base de datos (por si acaso) y creamos un proyecto de prueba
        projectRepository.deleteAll();

        testProject = new Project("Test Project", "Test Description", "Active", "Samuel Zapata");
        testProject = projectRepository.save(testProject);
    }

    // --- Tests para tu Tarea 4 ---

    @Test
    void testDeleteProject_Success() throws Exception {
        // 1. Arrange (Preparar)
        Long projectId = testProject.getId();

        // Verificamos que el proyecto existe en la DB antes de borrarlo
        assertThat(projectRepository.existsById(projectId)).isTrue();

        // 2. Act (Actuar)
        // Hacemos la llamada al endpoint DELETE /api/projects/{id}
        mockMvc.perform(delete("/api/projects/" + projectId))
                // 3. Assert (Verificar)
                .andExpect(status().isNoContent()); // Esperamos un HTTP 204 No Content

        // Verificación adicional: comprobamos que realmente fue borrado de la DB
        assertThat(projectRepository.existsById(projectId)).isFalse();
    }

    @Test
    void testDeleteProject_NotFound() throws Exception {
        // 1. Arrange (Preparar)
        Long nonExistentId = 999L;

        // Verificamos que el ID no existe
        assertThat(projectRepository.existsById(nonExistentId)).isFalse();

        // 2. Act (Actuar)
        // Hacemos la llamada al endpoint DELETE /api/projects/999
        mockMvc.perform(delete("/api/projects/" + nonExistentId))
                // 3. Assert (Verificar)
                .andExpect(status().isNotFound()); // Esperamos un HTTP 404 Not Found
    }
}