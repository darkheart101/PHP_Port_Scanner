package io.tkouleris.movieservice.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.tkouleris.movieservice.dto.otherResponse.RatingsResponse;
import io.tkouleris.movieservice.dto.response.ApiResponse;
import io.tkouleris.movieservice.dto.response.RatedMovie;
import io.tkouleris.movieservice.entity.Movie;
import io.tkouleris.movieservice.entity.Rating;
import io.tkouleris.movieservice.repository.IMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/movies")
public class MoviesController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IMovieRepository movieRepository;

    @GetMapping(path="/all", produces = "application/json")
//    @HystrixCommand(fallbackMethod = "getFallbackAllMovies")
    public ResponseEntity<Object> getAll(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMjcuMC4wLjE6ODAwMFwvYXBpXC9sb2dpbiIsImlhdCI6MTYyMDYxNzU1NSwiZXhwIjoxNjIwNjIxMTU1LCJuYmYiOjE2MjA2MTc1NTUsImp0aSI6IkRpRjIwTlE0OGhlbmx5QjQiLCJzdWIiOjEsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.YDO3hyZ9mNP7hr-ZQiQTPLJqb5GOxYGSHQmyf4wL1yU");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        System.out.println("AUTHENTICATION FIRST==========================");
        RestTemplate authRestTemplate = new RestTemplate();

        try {
            ResponseEntity<AuthResponse> response = authRestTemplate.exchange("http://127.0.0.1:8000/api/verify", HttpMethod.POST, entity,  AuthResponse.class);
//            AuthResponse authResponse = response.getBody();
            System.out.println(response.getBody().success);
            System.out.println(response.getBody().user.id);
            System.out.println(response.getBody().user.name);
//            ObjectMapper mapper = new ObjectMapper();
//            AuthResponse authResponse = mapper.readValue(response.getBody(), AuthResponse.class);
//            System.out.println(authResponse.success);
//            System.out.println(authResponse.user);
        }catch (Exception e){
            System.out.println("Not Authenticated...");
            System.out.println(e.getMessage());
        }



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

    public ResponseEntity<Object> getFallbackAllMovies(){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("No movies");
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }
}

class AuthResponse{
//    @JsonProperty("timestamp")
//    public String timestamp;
//    @JsonProperty("message")
//    public String message;
    @JsonProperty("success")
    public boolean success;
    @JsonProperty("user")
    public User user;

    public AuthResponse() {
    }
}

class User{
    public long id;
    public String name;
}