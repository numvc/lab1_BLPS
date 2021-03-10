package itmo.kinopoisk.demo.repo;

import itmo.kinopoisk.demo.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepo extends JpaRepository<Movie, String> {
    @Override
    List<Movie> findAll();

    List<Movie> findAllByCountryAndGenreAndYear(String country, String genre, int year);

    List<Movie> findAllByGenreAndYear(String genre, int year);

    List<Movie> findAllByGenre(String genre);

    List<Movie> findAllByYear(int year);

    List<Movie> findAllByCountry(String country);

    List<Movie> findAllByGenreAndCountry(String genre, String country);

    List<Movie> findAllByYearAndCountry(int year, String country);


    Movie findById(int id);

}
