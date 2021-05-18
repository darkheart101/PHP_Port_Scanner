package io.tkouleris.ratingsservice.controller;

import io.tkouleris.ratingsservice.dto.response.ApiResponse;
import io.tkouleris.ratingsservice.entity.Rating;
import io.tkouleris.ratingsservice.entity.User;
import io.tkouleris.ratingsservice.repository.IRatingRepository;
import io.tkouleris.ratingsservice.service.LoggedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/ratings")
public class RatingsController {

    @Autowired
    private IRatingRepository ratingRepository;

    @GetMapping(path="/all", produces = "application/json")
    public ResponseEntity<Object> getAll(){
        LoggedUserService loggedUserService = LoggedUserService.getInstance();
        User loggedInUser = loggedUserService.getLoggedInUser();
        if(loggedInUser == null){
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("Ratings");
            return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
        }
        List<Rating> ratings = ratingRepository.findRatingByUser((long)loggedInUser.getId());
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(ratings);
        apiResponse.setMessage("Ratings");
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }

}
