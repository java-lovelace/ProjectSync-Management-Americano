// Archivo: js/app-edit.js

const API_URL = "http://localhost:8080";
const form = document.getElementById('editProjectForm');
const btnUpdate = document.getElementById('btnUpdate');

// --- Utilidades de la Interfaz ---

// Muestra mensajes usando Toastify JS
function showToast(message, type = 'success') {
    Toastify({
        text: message,
        duration: 3000,
        close: true,
        gravity: "top",
        position: "right",
        stopOnFocus: true,
        style: {
            background: type === 'success' ? '#28a745' : '#dc3545',
        }
    }).showToast();
}

// Muestra u oculta el estado de carga y deshabilita el botón
function showLoading(isLoading) {
    if (isLoading) {
        btnUpdate.textContent = "Saving...";
        btnUpdate.disabled = true;
    } else {
        btnUpdate.textContent = "Save Changes";
        btnUpdate.disabled = false;
    }
}

// Función para obtener el ID del proyecto de la URL (Ej: edit-project.html?id=5)
function getProjectIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get('id');
}

// --- Lógica Principal ---

/**
 * Carga los datos actuales del proyecto y llena el formulario.
 */
async function loadProject(id) {
    if (!id) {
        showToast('Error: Project ID not found in URL.', 'error');
        return;
    }

    showLoading(true); // Usamos showLoading para deshabilitar el botón mientras carga

    try {
        // GET /api/projects/{id}
        const response = await fetch(`${API_URL}/api/projects/${id}`);

        if (response.status === 404) {
             throw new Error("Project not found.");
        }
        if (!response.ok) {
            throw new Error(`Error ${response.status}: Could not load project.`);
        }

        const project = await response.json();

        // Llenar el formulario con los datos actuales
        document.getElementById('projectId').value = project.id;
        document.getElementById('titleProject').value = project.title;
        document.getElementById('descriptionProject').value = project.description || ''; // Si es nulo, lo deja vacío
        document.getElementById('statusProject').value = project.status;
        document.getElementById('responsibleProject').value = project.responsiblePerson;

    } catch (error) {
        console.error("Error loading project:", error);
        showToast(`Failed to load project: ${error.message}`, 'error');
    } finally {
        showLoading(false); // Habilitar el botón una vez que la carga finalice (éxito o error)
    }
}

/**
 * Maneja el envío del formulario para actualizar el proyecto.
 */
async function handleEditFormSubmit(event) {
    event.preventDefault();

    const id = document.getElementById('projectId').value;

    // Recolectar datos del formulario
    const projectData = {
        title: document.getElementById('titleProject').value,
        description: document.getElementById('descriptionProject').value,
        status: document.getElementById('statusProject').value,
        responsiblePerson: document.getElementById('responsibleProject').value
    };

    showLoading(true);

    try {
        // PUT /api/projects/{id}
        const response = await fetch(`${API_URL}/api/projects/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(projectData)
        });

        const responseData = await response.json();

        if (response.ok) {
            showToast(`Project "${responseData.title}" updated successfully!`, 'success');

            // Opcional: Redirigir al listado después de una actualización exitosa
            setTimeout(() => window.location.href = 'index.html', 1500);

        } else if (response.status === 400) {
             // 400 Bad Request por fallos de @Valid en Spring (ej: campo vacío)
             // Nota: La estructura del error 400 puede variar. Se asume que Spring devuelve un mensaje.
             const errorMessage = responseData.message || responseData.error || 'Invalid data. Check the required fields.';
             showToast(`Validation Error: ${errorMessage}`, 'error');
        } else {
            throw new Error(responseData.message || `Unknown error (Status: ${response.status}).`);
        }

    } catch (error) {
        showToast(`Connection Failed: ${error.message}`, 'error');
        console.error('Error updating project:', error);
    } finally {
        // Solo habilitamos el botón si NO se redirigió (error o validación fallida)
        if (!response || !response.ok) {
            showLoading(false);
        }
    }
}


// --- Inicialización ---

document.addEventListener('DOMContentLoaded', () => {
    const projectId = getProjectIdFromUrl();

    // Cargar el proyecto
    loadProject(projectId);

    // Adjuntar el manejador de envío al formulario
    form.addEventListener('submit', handleEditFormSubmit);
});