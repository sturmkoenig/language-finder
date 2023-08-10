package org.lla.languagefinder.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity
public class Developer {
    @Id
    private String login;
}
