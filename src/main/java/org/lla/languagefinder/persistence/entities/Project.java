package org.lla.languagefinder.persistence.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "PROJECT")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @OneToOne
    private Developer developer;

    @OneToMany(mappedBy = "project")
    private Set<Language> language;

}
