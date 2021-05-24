package io.tkouleris.movieservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class RatingService {

    @Autowired
    private RestTemplate restTemplate;

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
        FileWriter myWriter = new FileWriter("/MyWork/Projects/Microservices/MicroservicesExample/cache/test.json");
        JSONObject json = new JSONObject(response.getBody().toString());
        myWriter.write(json.toString());
        myWriter.close();

        return response.getBody();
    }

    private String getToken() {
        TokenService tokenService = TokenService.getInstance();
        return tokenService.getToken();
    }

    public RatingsResponse getFallbackAllMovies() throws FileNotFoundException, JsonProcessingException {
        File file = new File("/MyWork/Projects/Microservices/MicroservicesExample/cache/test.json");
        Scanner myReader = new Scanner(file);
        RatingsResponse ratingsResponse = new RatingsResponse();
        while (myReader.hasNextLine()) {
            String response = myReader.nextLine();
            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            ratingsResponse = objectMapper.readValue(response, RatingsResponse.class);
            for (Rating r : ratingsResponse.data) {

                System.out.println(r);

            }
        }

        return ratingsResponse;
//        RatingsResponse response = new RatingsResponse();
//        Rating r = new Rating();
//        r.setMovie_id(1L);
//        r.setId(1);
//        r.setRate(10.0);
//        List<Rating> arr = new ArrayList<>();
//        arr.add(r);
//        response.data = arr;
//        response.timestamp = "xxx";
//
//        return response;
    }

    private HttpEntity setHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(headers);
    }


}
