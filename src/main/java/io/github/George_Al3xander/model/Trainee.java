package io.github.George_Al3xander.model;

import java.time.LocalDate;
import java.util.Objects;

public class Trainee extends User {
    private LocalDate dateOfBirth;
    private String address;
    private Long userId;

    public Trainee() {
    }

    public Trainee(LocalDate dateOfBirth, String address, Long userId) {
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.userId = userId;
    }

    public Trainee(
            Long id,
            String firstName,
            String lastName,
            String username,
            String password,
            boolean isActive,
            LocalDate dateOfBirth,
            String address,
            Long userId
    ) {
        super(id, firstName, lastName, username, password, isActive);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.userId = userId;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", userId='" + userId + '\'' +
                ", user=" + super.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trainee trainee)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(dateOfBirth, trainee.dateOfBirth)
                && Objects.equals(address, trainee.address)
                && Objects.equals(userId, trainee.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dateOfBirth, address, userId);
    }
}