const projectForm = document.getElementById("projectForm");
const title = document.getElementById("titleProject");
const description = document.getElementById("descriptionProject");
const responsible = document.getElementById("responsibleProject");

const API_URL = "http://localhost:8080";

projectForm.addEventListener("submit", async (event) => {
  event.preventDefault(); 
  
  try {
    const newProjectData = {
      title: capitalizeEachWord(title.value),
      description: description.value,
      status: "Active",
      responsiblePerson: capitalizeEachWord(responsible.value),
    };

    console.log(newProjectData);

    const response = await fetch(`${API_URL}/api/projects`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(newProjectData),
    });

    if (!response.ok) {
      notification("An error occurred.", "#b60404ff", 3000);
      const errorData = await response.json().catch(() => ({}));
      console.error("Error details:", errorData);
      throw new Error(errorData.message || "An error occurred.");
    }

    notification("Successful!", "#a7c957", 3000);
    resetForm();
    
    setTimeout(() => {
        window.location.href = "./index.html";
    }, 1000);

  } catch (error) {
    console.error("Error creating project:", error);
    notification("Error creating project", "#b60404ff", 3000);
  }
});

function resetForm() {
  projectForm.reset(); 
}

// VERSIÓN CORRECTA
const capitalizeEachWord = (str) => {
  if (!str) return '';
  return str
    .toLowerCase()
    .split(" ")
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join(" ");
};

const notification = (text, color, duration = 3000) => {
  Toastify({
    text,
    duration,
    close: true,
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