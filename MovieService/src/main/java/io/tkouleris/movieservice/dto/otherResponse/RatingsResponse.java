package io.tkouleris.movieservice.dto.otherResponse;

import io.tkouleris.movieservice.entity.Rating;

import java.util.List;

public class RatingsResponse {
    public List<Rating> data;
    public String timestamp;
    public String message;
}
