package io.tkouleris.movieservice.dto.otherResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.tkouleris.movieservice.entity.Rating;

import java.util.List;

public class RatingsResponse {
    @JsonProperty("data")
    public List<Rating> data;
    @JsonProperty("timestamp")
    public String timestamp;
    @JsonProperty("message")
    public String message;

    public String toString() {
        return data.toString();
    }
}
