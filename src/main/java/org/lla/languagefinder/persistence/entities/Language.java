package org.lla.languagefinder.persistence.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "LANGUAGE")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
