package itmo.kinopoisk.demo.services;

import itmo.kinopoisk.demo.entities.User;
import itmo.kinopoisk.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User getByLogin(String login){
        User user = userRepo.findByLogin(login);
        if(user == null){
            user = new User();

            user.setLogin(login);
            user.setSubed(false);
            userRepo.save(user);
        }
        return user;
    }

    public void changeSubStatus(String login){
        User user = userRepo.findByLogin(login);
        user.setSubed(true);
        userRepo.save(user);
    }
}
