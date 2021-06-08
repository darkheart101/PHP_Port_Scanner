package io.tkouleris.movieservice.controller;

import io.tkouleris.movieservice.dto.otherResponse.RatingsResponse;
import io.tkouleris.movieservice.dto.response.ApiResponse;
import io.tkouleris.movieservice.dto.response.RatedMovie;
import io.tkouleris.movieservice.entity.Movie;
import io.tkouleris.movieservice.entity.Rating;
import io.tkouleris.movieservice.repository.IMovieRepository;
import io.tkouleris.movieservice.service.Authentication;
import io.tkouleris.movieservice.service.LoggedUserService;
import io.tkouleris.movieservice.service.MovieService;
import io.tkouleris.movieservice.service.RatingService;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/movies")
public class MoviesController {

    private final IMovieRepository movieRepository;

    private final RatingService ratingService;

    private final MovieService movieService;

    public MoviesController(IMovieRepository movieRepository, RatingService ratingService, MovieService movieService) {
        this.movieRepository = movieRepository;
        this.ratingService = ratingService;
        this.movieService = movieService;
    }

    @GetMapping(path="/rated", produces = "application/json")
    public ResponseEntity<Object> getRatedMovies() throws IOException {
        RatingsResponse ratings = ratingService.getAll();

        if(ratings.data == null){
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("Movies");
            return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
        }

        List<Movie> movies = (List<Movie>) movieRepository.findAll();

        List<RatedMovie> ratedMovies = new ArrayList<>();
        for (Movie movie : movies) {
            for (Rating rating : ratings.data) {
                if(rating.getMovie_id() == movie.getId()){
                    RatedMovie ratedMovie = new RatedMovie();
                    ratedMovie.id = movie.getId();
                    ratedMovie.title = movie.getTitle();
                    ratedMovie.rate = rating.getRate();
                    ratedMovies.add(ratedMovie);
                }
            }
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(ratedMovies);
        apiResponse.setMessage("Movies");
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }

    @GetMapping(path="/unrated", produces = "application/json")
    public ResponseEntity<Object> getUnratedMovies() throws IOException {
        List<Movie> unratedMovies = movieService.getUnratedMovies();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(unratedMovies);
        apiResponse.setMessage("Unrated movies");
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }

    @GetMapping(path="/{movieId}", produces = "application/json")
    public ResponseEntity<Object> getMovie(@PathVariable("movieId") long movieId) throws IOException {

        Movie movie = this.movieService.getMovie(movieId);
        RatingsResponse ratingsResponse = this.ratingService.getMovieRating(movieId);
        if(ratingsResponse.data.get(0) != null){
            RatedMovie ratedMovie = new RatedMovie();
            ratedMovie.id = movie.getId();
            ratedMovie.title = movie.getTitle();
            ratedMovie.rate = ratingsResponse.data.get(0).rate;

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setData(ratedMovie);
            apiResponse.setMessage("Movie");
            return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
        }


        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(movie);
        apiResponse.setMessage("Movie");
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }

    @PostMapping(path="/add", produces = "application/json")
    public ResponseEntity<Object> addMovie(@RequestBody Movie movie){

        LoggedUserService loggedInService = LoggedUserService.getInstance();
        if(userIsNotAdmin(loggedInService)){
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setData(null);
            apiResponse.setMessage("Access to the requested resource is forbidden");
            return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.FORBIDDEN);
        }

        Movie createdMovie = this.movieService.addMovie(movie);
        String message = "Movie created";
        if(createdMovie == null){
            message = "Movie exists";
        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(createdMovie);
        apiResponse.setMessage(message);
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }

    private boolean userIsNotAdmin(LoggedUserService loggedInService) {
        return loggedInService.getLoggedInUser().getIs_admin() != 1;
    }
}


