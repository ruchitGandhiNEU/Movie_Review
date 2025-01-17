/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinema.service;

import com.cinema.DAO.AuthorizationDAO;
import com.cinema.Exceptions.ErrorsResponse;
import com.cinema.Exceptions.UserGeneratedExceptions;
import com.cinema.TokenResponse.JWTTokenResponse;
import com.cinema.TokenResponse.JWTTokenUtility;
import com.cinema.model.CustomMessage;
import com.cinema.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */

@Component
public class AuthService {
    
     @Autowired
    @Qualifier("authDao")
    AuthorizationDAO authDao;

    @Autowired
    @Qualifier("jwtTokenUtil")
    private JWTTokenUtility jwtTokenUtility;
    

   //For User Login , creating token and sending it back
    public ResponseEntity<Object> createAuthenticationToken(User u) throws Exception {
        try {
            User user = authDao.authenticate(u.getUserName(), u.getPassword());
            final String token = jwtTokenUtility.generateToken(user);
            System.out.println("com.cinema.controller.AuthController.createAuthenticationToken() - /login -- user " + user.getUserName());
            return new ResponseEntity<>(new JWTTokenResponse(token,user), HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }

    // Getting user details from token
    // access: private
    public ResponseEntity<Object> getUserDetails(HttpServletRequest request) throws Exception {
        try {
            User user = (User) request.getAttribute("user");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }
    
}
