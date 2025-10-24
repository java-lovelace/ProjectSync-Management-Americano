package com.americano.ProjectSync.Management.controller;

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