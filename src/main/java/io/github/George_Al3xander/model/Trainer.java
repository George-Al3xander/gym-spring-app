package io.github.George_Al3xander.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Trainer extends User {

    private Long userId;

    private TrainingType specialization;

    public Trainer(
            String firstName,
            String lastName,
            String username,
            String password,
            boolean isActive,
            Long userId,
            TrainingType specialization
    ) {
        super(firstName, lastName, username, password, isActive);
        setUserId(userId);
        setSpecialization(specialization);
    }
}