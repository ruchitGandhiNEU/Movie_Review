/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinema.controller;




import com.cinema.model.Password;
import com.cinema.model.User;
import com.cinema.service.UserService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */

@RestController
@RequestMapping("/api")
public class UserController {
    
    
    @Autowired
    @Qualifier("userService")
    UserService userService;


    // User registeration -> this method will be used for registering the user and assigning back a token using JWT bean.
    //This will also retur a user for reference of ID
    @RequestMapping(value = "/auth/register",
            method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Object> register(@RequestBody User user, BindingResult result) throws Exception {
        return userService.register(user, result);
    }
    
    
    
     //This method will be used to change the password of the user, it will take user password a string in the body and user from the token. Thus validating the same.
    @RequestMapping(value = "/auth/changepass",
            method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Object> changePassword(@RequestBody Password password, @RequestHeader HttpHeaders headers) throws Exception {
        return userService.changePassword(password, headers);
    }
    

    // This will enable us to get user using a user ID for mapping purpose in future.
    //All errors are handled in service
    @RequestMapping(value = "/users/{userId}",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<Object> getUser(@PathVariable String userId, @RequestHeader HttpHeaders headers) throws Exception {
        return userService.getUser(userId, headers);
    }

    // If we want to search for a user uising username,  we can do that here.
    @RequestMapping(value = "/users/search/{userName}",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<Object> searchUsers(@PathVariable String userName) throws Exception {
        return userService.searchUsers(userName);
    }

}
