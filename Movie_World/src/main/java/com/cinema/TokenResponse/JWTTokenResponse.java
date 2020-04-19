/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinema.TokenResponse;

import com.cinema.model.User;

/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */
public class JWTTokenResponse {
    
    private final String token;
    private User user;
    
    public JWTTokenResponse(String jwttoken,User user) {
        this.token = jwttoken;
        this.user = user;
    }
    
    public String getToken() {
        return this.token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
}
