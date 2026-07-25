package io.github.George_Al3xander.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trainers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Trainer extends User {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "specialization")
    private TrainingType specialization;

    public Trainer(
            Long id,
            String firstName,
            String lastName,
            String username,
            String password,
            boolean isActive,
            TrainingType specialization
    ) {
        super(id, firstName, lastName, username, password, isActive);
        setSpecialization(specialization);
    }
}