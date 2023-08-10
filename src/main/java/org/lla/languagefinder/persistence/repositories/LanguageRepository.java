package org.lla.languagefinder.persistence.repositories;

import org.lla.languagefinder.persistence.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}
