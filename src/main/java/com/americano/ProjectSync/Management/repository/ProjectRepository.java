package com.americano.ProjectSync.Management.repository;

import com.americano.ProjectSync.Management.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {
}
