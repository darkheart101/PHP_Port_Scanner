package io.tkouleris.movieservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MoviesController {

    @GetMapping(path="/all", produces = "application/json")
    public ResponseEntity<Object> getAll(){
        List<Integer> myList = new ArrayList<>();
        myList.add(1);
        myList.add(666);

        return new ResponseEntity<>(myList, HttpStatus.OK);
    }
}
