package io.tkouleris.movieservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.tkouleris.movieservice.dto.otherResponse.AuthResponse;
import io.tkouleris.movieservice.dto.otherResponse.RatingsResponse;
import io.tkouleris.movieservice.dto.response.ApiResponse;
import io.tkouleris.movieservice.entity.Rating;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Service
public class RatingService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CacheService cacheService;

    @HystrixCommand(fallbackMethod = "getFallbackAllMovies")
    public RatingsResponse getAll() throws IOException {
        String token = getToken();
        var entity = this.setHeaders(token);
        ResponseEntity<RatingsResponse> response = restTemplate.exchange(
                "http://ratings-service/ratings/all",
                HttpMethod.GET,
                entity,
                RatingsResponse.class
        );
        // cache
        this.cacheService.save(response.getBody().toString(),"test");

        return response.getBody();
    }

    private String getToken() {
        TokenService tokenService = TokenService.getInstance();
        return tokenService.getToken();
    }

    public RatingsResponse getFallbackAllMovies() throws FileNotFoundException, JsonProcessingException {
        System.out.println(Rating.class);
//        File file = new File("/MyWork/Projects/Microservices/MicroservicesExample/cache/test.json");
//        Scanner myReader = new Scanner(file);
//        RatingsResponse ratingsResponse = new RatingsResponse();
//        Rating[] ratings = new Rating[0];
//        while (myReader.hasNextLine()) {
//            String response = myReader.nextLine();
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//            ratings = objectMapper.readValue(response, Rating[].class);
//        }
        Rating[] ratings = new Rating[0];
//        ratings = this.cacheService.getKey("test", ratings);
//        for(Rating r: ratings){
//            System.out.println(r.Id);
//            System.out.println(r.rate);
//        }
        RatingsResponse ratingsResponse = new RatingsResponse();
        ratingsResponse.data = new ArrayList<>();
//        ratingsResponse.data.addAll(Arrays.asList(ratings));
        ratingsResponse.message = "Ratings";
        ratingsResponse.timestamp = LocalDateTime.now().toString();
        return ratingsResponse;

    }

    private HttpEntity setHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(headers);
    }


}
