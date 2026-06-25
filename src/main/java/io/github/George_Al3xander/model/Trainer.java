package io.github.George_Al3xander.model;

import java.util.Objects;

public class Trainer extends User {
    private Long userId;
    private TrainingType specialization;

    public Trainer() {
    }

    public Trainer(Long userId, TrainingType specialization) {
        this.userId = userId;
        this.specialization = specialization;
    }

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
        this.userId = userId;
        this.specialization = specialization;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "userId='" + userId + '\'' +
                ", specialization='" + specialization + '\'' +
                ", user=" + super.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trainer trainer)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(userId, trainer.userId)
                && Objects.equals(specialization, trainer.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, specialization);
    }
}