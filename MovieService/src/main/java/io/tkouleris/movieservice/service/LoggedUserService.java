package io.tkouleris.movieservice.service;

import io.tkouleris.movieservice.entity.User;

public class LoggedUserService {

    private static LoggedUserService instance = null;
    private User loggedInUser = null;

    private LoggedUserService() {
    }

    public static LoggedUserService getInstance() {
        if (LoggedUserService.instance == null) {
            LoggedUserService.instance = new LoggedUserService();
        }
        return LoggedUserService.instance;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public User getLoggedInUser() {
        return this.loggedInUser;
    }
}
