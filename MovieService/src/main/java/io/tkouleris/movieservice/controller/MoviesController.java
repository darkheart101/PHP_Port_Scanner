package io.tkouleris.movieservice.controller;

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

    @GetMapping(path="/all", produces = "application/json")
    public ResponseEntity<Object> getAll(){

        Ratings ratings = restTemplate.getForObject("http://ratings-service/ratings/all",Ratings.class);
        List<Integer> myList = new ArrayList<>();
        myList.add(1);
        myList.add(666);
        myList.add(ratings.data.get(1));
        myList.add(ratings.data.get(0));

        return new ResponseEntity<>(myList, HttpStatus.OK);
    }
}

class Ratings{
    public List<Integer> data;
    public String timestamp;
    public String message;
}
