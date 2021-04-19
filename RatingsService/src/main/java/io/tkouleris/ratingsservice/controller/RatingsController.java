package io.tkouleris.ratingsservice.controller;

import io.tkouleris.ratingsservice.dto.response.ApiResponse;
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

    @GetMapping(path="/all", produces = "application/json")
    public ResponseEntity<Object> getAll(){
        List<Integer> myList = new ArrayList<>();
        myList.add(10000);
        myList.add(60000);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(myList);
        apiResponse.setMessage("Ratings");
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }

}
