package com.example.onlinefooddeliverysb.service;

import com.example.onlinefooddeliverysb.model.User;

public interface UserService {

    public User findUserByJwtToken(String jwt) throws Exception;

    public User findUserByEmail(String email)throws Exception;
}
