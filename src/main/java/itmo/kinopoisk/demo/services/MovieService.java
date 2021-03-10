package itmo.kinopoisk.demo.services;

import itmo.kinopoisk.demo.entities.Movie;
import itmo.kinopoisk.demo.repo.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    @Autowired
    private MovieRepo movieRepo;

    public List<Movie> getAllMovies(Optional<String> genre, Optional<Integer> year, Optional<String> country) {
        if (genre.isPresent() && year.isPresent() && country.isPresent())
            return movieRepo.findAllByCountryAndGenreAndYear(country.get(), genre.get(), year.get());

        if (genre.isPresent() && year.isPresent() && !country.isPresent())
            return movieRepo.findAllByGenreAndYear(genre.get(), year.get());

        if (genre.isPresent() && !year.isPresent() && !country.isPresent())
            return movieRepo.findAllByGenre(genre.get());

        if (genre.isPresent() && !year.isPresent() && country.isPresent())
            return movieRepo.findAllByGenreAndCountry(genre.get(), country.get());

        if (!genre.isPresent() && year.isPresent() && country.isPresent())
            return movieRepo.findAllByYearAndCountry(year.get(), country.get());

        if (!genre.isPresent() && year.isPresent() && !country.isPresent())
            return movieRepo.findAllByYear(year.get());

        if (!genre.isPresent() && !year.isPresent() && country.isPresent())
            return movieRepo.findAllByCountry(country.get());

        return movieRepo.findAll();
    }

    public Movie getMovieById(int id) {
        return movieRepo.findById(id);
    }

    public void changeRaitById(int id, double currRate) {
        Movie movie = movieRepo.findById(id);
        if (currRate > movie.getRate())
            movie.setRate(movie.getRate() + currRate / 100);
        else
            movie.setRate(movie.getRate() - currRate / 100);
        movieRepo.save(movie);
    }

    public String getMovieDesc(int id){
        Movie movie = movieRepo.findById(id);
        return movie.getName() + ", "+ movie.getYear() + '\n' + movie.getDescription();
    }

}
