package io.github.George_Al3xander.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Trainee extends User {

    private LocalDate dateOfBirth;

    private String address;

    private Long userId;

    public Trainee(
            String firstName,
            String lastName,
            String username,
            String password,
            boolean isActive,
            LocalDate dateOfBirth,
            String address,
            Long userId
    ) {
        super(firstName, lastName, username, password, isActive);
        setDateOfBirth(dateOfBirth);
        setAddress(address);
        setUserId(userId);
    }
}