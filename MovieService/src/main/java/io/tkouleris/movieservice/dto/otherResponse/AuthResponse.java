package io.tkouleris.movieservice.dto.otherResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.tkouleris.movieservice.entity.User;

public class AuthResponse {
    public boolean success;
    public User user;
}
