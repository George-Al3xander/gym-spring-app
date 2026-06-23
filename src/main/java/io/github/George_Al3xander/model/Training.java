package io.github.George_Al3xander.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Training {
    private String traineeId;
    private String trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDateTime trainingDate;
    private Integer durationSeconds;

    public Training() {
    }

    public Training(String traineeId, String trainerId, String trainingName, TrainingType trainingType, LocalDateTime trainingDate, Integer durationSeconds) {
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.durationSeconds = durationSeconds;
    }

    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public LocalDateTime getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDateTime trainingDate) {
        this.trainingDate = trainingDate;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    @Override
    public String toString() {
        return "Training{" +
                "traineeId=" + traineeId +
                ", trainerId=" + trainerId +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + trainingType +
                ", trainingDate=" + trainingDate +
                ", durationSeconds=" + durationSeconds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Training training)) return false;
        return Objects.equals(traineeId, training.traineeId)
                && Objects.equals(trainerId, training.trainerId)
                && Objects.equals(trainingName, training.trainingName)
                && trainingType == training.trainingType
                && Objects.equals(trainingDate, training.trainingDate)
                && Objects.equals(durationSeconds, training.durationSeconds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traineeId, trainerId, trainingName, trainingType, trainingDate, durationSeconds);
    }
}