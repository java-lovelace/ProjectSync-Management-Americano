# **ProjectSync - CRUDACTIVITY**

ProjectSync is an internal tool designed to register and track active projects within an organization. This system manages essential project information such as name, status, description, and the responsible person.

This project is part of the "CRUDACTIVITY" challenge, focusing on applying technical and professional skills, including database integration, ORM, Git Flow, and Azure DevOps traceability.

## 1. Technical Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3+ (Web, Data JPA, Validation)
* **Database:** PostgreSQL (via Supabase)
* **ORM:** JPA / Hibernate (Spring Data JPA)
* **Testing:** JUnit 5 (Unit & Integration)
* **Frontend:** HTML5, JavaScript (ES6+), Bootstrap
* **Version Control:** Git (Git Flow)
* **Work Management:** Azure DevOps (Boards + Repos)

## 2. Project Requirements

### Functional Requirements
* Connect the backend to a PostgreSQL database.
* Implement the complete CRUD flow (Create, Read, Update, Delete) for projects.
* Validate all required fields and handle HTTP errors.
* Include auditing fields for creation (`createdDate`) and update (`lastModifiedDate`) timestamps.
* Build a simple frontend to interact with the backend API.

### Technical & Quality Requirements
* Unit tests for core business logic (Services).
* Integration tests for the REST API (Controllers) and database connectivity.
* All code, endpoints, and structures must follow English naming conventions.
* Full work traceability through Azure DevOps (Work Items linked to commits, branches, and PRs).

## 3. How to Run This Project

### Prerequisites
* Java 21 (JDK)
* Apache Maven 3.8+
* A running PostgreSQL database (e.g., on [Supabase](https://supabase.com/))

### Backend Setup (Spring Boot)

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/java-lovelace/ProjectSync-Management-Americano.git
    cd ProjectSync-Management-Americano
    ```

2.  **Configure Database:**
    * Open the `src/main/resources/application.properties` file.
    * Update the following properties with your Supabase/PostgreSQL credentials:
        ```properties
        spring.datasource.url=jdbc:postgresql://aws-1-us-west-1.pooler.supabase.com:6543/postgres
        spring.datasource.username=postgres.rcqapamuxlqnqucmxwru
        spring.datasource.password=
        ```

3.  **Run the application (via Maven):**
    ```bash
    mvn spring-boot:run
    ```
    The API will be available at `http://localhost:8080`.

### Frontend Access
Once the backend is running, you can access the frontend by opening this URL in your browser:
* `http://localhost:8080/index.html`

### Key API Endpoints 📍

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/projects` | Creates a new project. |
| `GET` | `/api/projects` | Retrieves a list of all projects. |
| `GET` | `/api/projects/{id}` | Retrieves a single project by its ID. |
| `PUT` | `/api/projects/{id}` | Updates an existing project. |
| `DELETE` | `/api/projects/{id}` | Deletes a project by its ID. |

## 4. Team Roles & Responsibilities

This project follows a vertical-slicing approach, where each team member is responsible for an end-to-end CRUD feature.

| Feature | Responsible Member | Responsibilities |
| :--- | :--- | :--- |
| **CREATE (C)** | `Cristian Penagos` | `create-project.html`, `POST /api/projects` endpoint, service logic, and tests. |
| **READ (R)** | `Camila Acosta` | `index.html` list, `GET /api/projects` endpoints, service logic, and tests. |
| **UPDATE (U)** | `Andres Gonzales` | `edit-project.html`, `PUT /api/projects/{id}` endpoint, service logic, and tests. |
| **DELETE (D)** | `Samuel Zapata` | Delete button (in `index.html`), `DELETE /api/projects/{id}` endpoint, service logic, and tests. |
| **Setup & DevOps**| (Team) | Initial project setup, DB schema, and `README`. |