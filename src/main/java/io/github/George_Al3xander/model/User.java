package io.github.George_Al3xander.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private boolean isActive;
}