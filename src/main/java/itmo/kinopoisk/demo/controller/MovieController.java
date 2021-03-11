package itmo.kinopoisk.demo.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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


    @ApiOperation(value = "${MovieController.getMovieList}")
    @RequestMapping(value = "movielist",
            method = RequestMethod.GET,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<Iterable<Movie>> getMovieList(@ApiParam("genre")   @RequestParam(value = "genre") Optional<String> genre,
                                                        @ApiParam("year")    @RequestParam(value = "year") Optional<Integer> year,
                                                        @ApiParam("country") @RequestParam(value = "country") Optional<String> country) {
        try {
            return new ResponseEntity<>(movieService.getAllMovies(genre, year, country), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }


    @ApiOperation(value = "${MovieController.rateMovie}")
    @RequestMapping(value = "ratemovie",
            method = RequestMethod.POST,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<Movie> rateMovie(@ApiParam("currRate") Optional<Double> currRate, @ApiParam("movieId") Optional<Integer> movieId) {
        if(!movieId.isPresent())
            return new ResponseEntity("MovieId is required!", HttpStatus.BAD_REQUEST);

        if(!currRate.isPresent())
            return new ResponseEntity("currRate is required!", HttpStatus.BAD_REQUEST);

        if (currRate.get() > 10)
            return new ResponseEntity("Rate can't be more than 10", HttpStatus.BAD_REQUEST);

        movieService.changeRaitById(movieId.get(), currRate.get());

        try {
            return new ResponseEntity<>(movieService.getMovieById(movieId.get()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }


    @ApiOperation(value = "${MovieController.watchMovie}")
    @RequestMapping(value = "watchmovie",
            method = RequestMethod.POST,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<String> watchMovie(@ApiParam("movieId") Optional<Integer> movieId, @ApiParam("login") Optional<String> login) {
        if (!movieId.isPresent())
            return new ResponseEntity<String>("MovieId is required!", HttpStatus.BAD_REQUEST);

        if(!login.isPresent())
            return new ResponseEntity<String>("Login is required!", HttpStatus.BAD_REQUEST);

        if (userService.getByLogin(login.get()).isSubed() || movieService.getMovieById(movieId.get()).isFree())
            return new ResponseEntity<String>(movieService.getMovieDesc(movieId.get()), HttpStatus.OK);

        return new ResponseEntity<String>("You have to buy a subscription for 150 rubles!", HttpStatus.FORBIDDEN);
    }


    @ApiOperation(value = "${MovieController.buySub}")
    @RequestMapping(value = "buysub",
            method = RequestMethod.POST,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE},
            headers = "Accept=application/json")
    public ResponseEntity<String> buySub(@ApiParam("login") Optional<String> login, @ApiParam("sum") Optional<Integer> sum){
        if(!login.isPresent())
            return new ResponseEntity<String>("Login is required!", HttpStatus.BAD_REQUEST);

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
