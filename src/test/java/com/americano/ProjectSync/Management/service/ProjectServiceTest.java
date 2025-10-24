package com.americano.ProjectSync.Management.service;


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

}