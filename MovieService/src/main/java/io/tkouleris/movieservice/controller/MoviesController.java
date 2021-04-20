package io.tkouleris.movieservice.controller;

import io.tkouleris.movieservice.dto.otherResponse.RatingsResponse;
import io.tkouleris.movieservice.dto.response.ApiResponse;
import io.tkouleris.movieservice.dto.response.RatedMovie;
import io.tkouleris.movieservice.entity.Movie;
import io.tkouleris.movieservice.entity.Rating;
import io.tkouleris.movieservice.repository.IMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MoviesController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IMovieRepository movieRepository;

    @GetMapping(path="/all", produces = "application/json")
    public ResponseEntity<Object> getAll(){

        RatingsResponse ratings = restTemplate.getForObject("http://ratings-service/ratings/all",RatingsResponse.class);

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
}
