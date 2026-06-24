package io.github.George_Al3xander.service;

import io.github.George_Al3xander.dao.TraineeDao;
import io.github.George_Al3xander.dao.TrainerDao;
import io.github.George_Al3xander.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsernameGenerator {
    @Autowired
    private TraineeDao traineeDao;
    @Autowired
    private TrainerDao trainerDao;


    public String generateUsername(User user) {
        String baseUsername = buildBaseUsername(user);

        String username = baseUsername;
        int suffix = 1;

        while (usernameExists(username)) {
            username = baseUsername + suffix++;
        }

        return username;
    }

    private String buildBaseUsername(User user) {
        return user.getFirstName().trim() + "." + user.getLastName().trim();
    }

    private boolean usernameExists(String username) {
        boolean existsInTrainees = traineeDao.findAll().stream()
                .anyMatch(u -> username.equalsIgnoreCase(u.getUsername()));

        boolean existsInTrainers = trainerDao.findAll().stream()
                .anyMatch(u -> username.equalsIgnoreCase(u.getUsername()));

        return existsInTrainees
                || existsInTrainers;
    }
}
