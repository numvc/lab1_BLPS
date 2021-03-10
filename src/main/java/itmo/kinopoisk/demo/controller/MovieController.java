package itmo.kinopoisk.demo.controller;

import itmo.kinopoisk.demo.entities.Movie;
import itmo.kinopoisk.demo.services.MovieService;
import itmo.kinopoisk.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "movielist",
            method = RequestMethod.GET,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<Iterable<Movie>> getMovieList(@RequestParam(value = "genre") Optional<String> genre,
                                                        @RequestParam(value = "year") Optional<Integer> year,
                                                        @RequestParam(value = "country") Optional<String> country) {
        try {
            return new ResponseEntity<>(movieService.getAllMovies(genre, year, country), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "ratemovie",
            method = RequestMethod.POST,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<Movie> rateMovie(@RequestParam(value = "rate") double currRate, @RequestParam(value = "id") int movieId) {

        if (currRate > 10)
            return new ResponseEntity("Rate can't be more than 10", HttpStatus.BAD_REQUEST);

        movieService.changeRaitById(movieId, currRate);

        try {
            return new ResponseEntity<>(movieService.getMovieById(movieId), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "watchmovie",
            method = RequestMethod.POST,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<String> watchMovie(Optional<Integer> movieId, Optional<String> login) {
        if (!movieId.isPresent())
            return new ResponseEntity<String>("MovieId is required!", HttpStatus.BAD_REQUEST);

        if(!login.isPresent())
            return new ResponseEntity<String>("Login is required!", HttpStatus.FORBIDDEN);

        if (userService.getByLogin(login.get()).isSubed() || movieService.getMovieById(movieId.get()).isFree())
            return new ResponseEntity<String>(movieService.getMovieDesc(movieId.get()), HttpStatus.OK);

        return new ResponseEntity<String>("You have to buy a subscription for 150 rubles!", HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "buysub",
            method = RequestMethod.POST,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<String> buySub(Optional<String> login, Optional<Integer> sum){
        if(userService.getByLogin(login.get()).isSubed())
            return new ResponseEntity<String>("You already have a subscription! Refunded", HttpStatus.OK);

        if(!sum.isPresent())
            return new ResponseEntity<String>("Sum is required!", HttpStatus.BAD_REQUEST);

        if(sum.get() >= 150){
            userService.changeSubStatus(login.get());
            return new ResponseEntity<String>("Enjoy the movies!", HttpStatus.OK);
        }
        return new ResponseEntity<String>("Insufficient funds", HttpStatus.OK);

    }



}
