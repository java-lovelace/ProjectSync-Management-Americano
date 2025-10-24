const API_URL = "http://localhost:8080";
const tableBody = document.getElementById("projectTableBody");
const loadingMessage = document.getElementById("loadingMessage");

// Run project loading when the DOM is ready
document.addEventListener("DOMContentLoaded", () => {
    loadProjects();
});


// Loads projects from the API and displays them in the table

async function loadProjects() {
    showLoading(true);
    try {
        const response = await fetch(`${API_URL}/api/projects`);

        if (!response.ok) {
            throw new Error(`Error ${response.status}: Could not fetch projects.`);
        }

        const projects = await response.json();
        renderProjectTable(projects);

    } catch (error) {
        console.error("Error loading projects:", error);
        showError("Could not load projects. Please try again later.");
    } finally {
        showLoading(false);
    }
}

// Show or hide the loading message
function showLoading(isLoading) {
    if (isLoading) {
        tableBody.innerHTML = ""; // Limpiar tabla mientras carga
        loadingMessage.style.display = "block";
        loadingMessage.textContent = "Loading projects...";
    } else {
        loadingMessage.style.display = "none";
    }
}

// Displays an error message in the table
function showError(message) {
    tableBody.innerHTML = `
        <tr>
            <td colspan="6" class="text-center text-danger">${message}</td>
        </tr>
    `;
}

function renderProjectTable(projects) {
    // Clean the table before adding new rows
    tableBody.innerHTML = "";

    if (projects.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center">No projects found.</td>
            </tr>
        `;
        return;
    }

    projects.forEach(project => {
        const row = document.createElement("tr");

        // Format the date to make it readable
        const lastModified = new Date(project.lastModifiedDate).toLocaleString();

        row.innerHTML = `
            <td>${project.id}</td>
            <td>${project.title}</td>
            <td><span class="badge bg-info text-dark">${project.status}</span></td>
            <td>${project.responsiblePerson}</td>
            <td>${lastModified}</td>
            <td>
                <a href="./edit-project.html?id=${project.id}" class="btn btn-warning btn-sm">Edit</a>
                <button class="btn btn-danger btn-sm" onclick="deleteProject(${project.id})">Delete</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

const notification = (text, color, duration = 3000) => {
  Toastify({
    text,
    duration,
    close: false,
    gravity: "top",
    position: "center",
    stopOnFocus: true,
    style: {
      borderRadius: "8px",
      padding: "15px",
      background: color,
    },
  }).showToast();
};

// Función para MOSTRAR la confirmación
function deleteProject(id, title) {

    // Nodo HTML que irá dentro de la notificación
    const toastNode = document.createElement('div');
    toastNode.innerHTML = `
        <div class="text-center">
            <strong>Are you sure you want to delete</strong>
            <div class="mt-3">
                <button class="btn btn-danger btn-sm me-2" id="toast-confirm-btn-${id}">Yes, Delete</button>
                <button class="btn btn-secondary btn-sm" id="toast-cancel-btn-${id}">Cancel</button>
            </div>
        </div>
    `;

    // Instancia del Toast
    const toastInstance = Toastify({
        node: toastNode,
        duration: 3000,
        gravity: "top",
        position: "center",
        close: false,
        style: {
            background: "linear-gradient(to right, #ffc107, #e8a700)",
            color: "#000",
            borderRadius: "8px",
            padding: "15px",
            boxShadow: "0 3px 6px rgba(0,0,0,0.16)",
        }
    });

    // Listeners a los botones
    const confirmBtn = toastNode.querySelector(`#toast-confirm-btn-${id}`);
    const cancelBtn = toastNode.querySelector(`#toast-cancel-btn-${id}`);

    confirmBtn.addEventListener('click', () => {
        executeDelete(id);
        toastInstance.hideToast();
    });

    cancelBtn.addEventListener('click', () => {
        toastInstance.hideToast();
    });

    toastInstance.showToast();
}

// La lógica de borrado
async function executeDelete(id) {
    try {
        const response = await fetch(`${API_URL}/api/projects/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) { // HTTP 204 No Content
            notification("Project deleted successfully!", "#a7c957", 3000);
            await loadProjects();
        } else if (response.status === 404) {
            notification(`Error: Project with ID ${id} not found.`, "#b60404ff");
        } else {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

    } catch (error) {
        console.error('Error deleting project:', error);
        notification("Error deleting project. Please try again later.", "#b60404ff", 3000);
    }
}