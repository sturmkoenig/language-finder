package org.lla.languagefinder.persistence.repositories;

import org.lla.languagefinder.persistence.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
