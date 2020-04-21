/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates




* and open the template in the editor.
 */
package com.cinema.controller;



import com.cinema.DAO.AuthorizationDAO;
import com.cinema.Exceptions.ErrorsResponse;
import com.cinema.Exceptions.UserGeneratedExceptions;
import com.cinema.TokenResponse.JWTTokenResponse;
import com.cinema.TokenResponse.JWTTokenUtility;
import com.cinema.model.CustomMessage;
import com.cinema.model.User;
import com.cinema.service.AuthService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    @Qualifier("authService")
    AuthService authService;

    //This method is for login of user and generating token, using Token Utility.
    //Thus Token Might be used for later purpose
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes="application/json")
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody User u) throws Exception {
        return authService.createAuthenticationToken(u);
    }

//This method is for getting userdetails just from the request
    @RequestMapping(value = "/getUserDetails", method = RequestMethod.GET)
    public ResponseEntity<Object> getUserDetails(HttpServletRequest request) throws Exception {
            return authService.getUserDetails(request);
    }
}
