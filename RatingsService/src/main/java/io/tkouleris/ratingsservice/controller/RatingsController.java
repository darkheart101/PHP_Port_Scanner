package io.tkouleris.ratingsservice.controller;

import io.tkouleris.ratingsservice.dto.response.ApiResponse;
import io.tkouleris.ratingsservice.entity.Rating;
import io.tkouleris.ratingsservice.repository.IRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ratings")
public class RatingsController {

    @Autowired
    private IRatingRepository ratingRepository;

    @GetMapping(path="/all", produces = "application/json")
    public ResponseEntity<Object> getAll(){
        List<Rating> ratings = (List<Rating>) ratingRepository.findAll();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(ratings);
        apiResponse.setMessage("Ratings");
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }

}
