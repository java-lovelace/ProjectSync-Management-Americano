package com.americano.ProjectSync.Management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProjectSyncManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectSyncManagementApplication.class, args);
	}

}
