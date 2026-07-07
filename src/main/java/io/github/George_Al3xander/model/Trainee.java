package io.github.George_Al3xander.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trainees")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Trainee extends User {
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column
    private String address;

    @OneToMany(
            mappedBy = "trainee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Training> trainings = new ArrayList<>();

    public Trainee(
            Long id,
            String firstName,
            String lastName,
            String username,
            String password,
            boolean isActive,
            LocalDate dateOfBirth,
            String address
    ) {
        super(id, firstName, lastName, username, password, isActive);
        setDateOfBirth(dateOfBirth);
        setAddress(address);
    }
}