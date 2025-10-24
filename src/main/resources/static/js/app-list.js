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

function deleteProject(id) {
    console.warn(`Delete functionality for id ${id} is not implemented yet.`);
    alert("Delete functionality is not implemented yet.");
}