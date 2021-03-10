package itmo.kinopoisk.demo.repo;

import itmo.kinopoisk.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, String> {
    User findByLogin(String login);
}
