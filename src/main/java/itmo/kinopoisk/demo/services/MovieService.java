package itmo.kinopoisk.demo.services;

import itmo.kinopoisk.demo.entities.Movie;
import itmo.kinopoisk.demo.repo.MovieRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepo movieRepo;

    public MovieService(MovieRepo movieRepo) {
        this.movieRepo = movieRepo;
    }

    //not sure about 7if decision. hope you can help me
    public List<Movie> getMoviesByParam(Optional<String> genre, Optional<Integer> year, Optional<String> country) {
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

    public void changeMovieRaitById(int id, double currRate) {
        Movie movie = movieRepo.findById(id);
        double movieRate;
        if (currRate > movie.getRate())
            movieRate = movie.getRate() + currRate / 100;
        else
            movieRate = movie.getRate() - currRate / 100;

        movie.setRate(movieRate);
        movieRepo.save(movie);
    }

    public String getMovieDesc(int id) {
        Movie movie = movieRepo.findById(id);
        return movie.getName() + ", " + movie.getYear() + '\n' + movie.getDescription();
    }

}
