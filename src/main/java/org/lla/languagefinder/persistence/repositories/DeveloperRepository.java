package org.lla.languagefinder.persistence.repositories;

import org.lla.languagefinder.persistence.entities.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer, String> {
}
