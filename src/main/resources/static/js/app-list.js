const API_URL = "http://localhost:8080";
const tableBody = document.getElementById("projectTableBody");
const loadingMessage = document.getElementById("loadingMessage");

// Ejecutar la carga de proyectos cuando el DOM esté listo
document.addEventListener("DOMContentLoaded", () => {
    loadProjects();
});


// Carga los proyectos desde la API y los muestra en la tabla.

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

// Muestra u oculta el mensaje de carga.
function showLoading(isLoading) {
    if (isLoading) {
        tableBody.innerHTML = ""; // Limpiar tabla mientras carga
        loadingMessage.style.display = "block";
        loadingMessage.textContent = "Loading projects...";
    } else {
        loadingMessage.style.display = "none";
    }
}

// Muestra un mensaje de error en la tabla.
function showError(message) {
    tableBody.innerHTML = `
        <tr>
            <td colspan="6" class="text-center text-danger">${message}</td>
        </tr>
    `;
}

function renderProjectTable(projects) {
    // Limpiar la tabla antes de agregar nuevas filas
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

        // Formatear la fecha para que sea legible
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

function deleteProject(id) {
    console.warn(`Delete functionality for id ${id} is not implemented yet.`);
    alert("Delete functionality is not implemented yet.");
}