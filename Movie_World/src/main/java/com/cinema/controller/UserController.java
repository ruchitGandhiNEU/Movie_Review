/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinema.controller;




import com.cinema.DAO.UserHandlerDAO;
import com.cinema.Exceptions.ErrorsResponse;
import com.cinema.Exceptions.UserGeneratedExceptions;
import com.cinema.TokenResponse.JWTTokenResponse;
import com.cinema.TokenResponse.JWTTokenUtility;
import com.cinema.model.CustomMessage;
import com.cinema.model.Password;
import com.cinema.model.User;
import com.cinema.model.UserValidationsCheck;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */

@RestController
@RequestMapping("/api")
public class UserController {
    
    
    @Autowired
    @Qualifier("userDao")
    UserHandlerDAO userDao;

    @Autowired
    @Qualifier("userValidator")
    UserValidationsCheck validator;

    @Autowired
    @Qualifier("jwtTokenUtil")
    private JWTTokenUtility jwtTokenUtil;

    @InitBinder("user")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    // User registeration
    @RequestMapping(value = "/auth/register",
            method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Object> register(@RequestBody User user, BindingResult result) throws Exception {
        // Validting fields in form
        validator.validate(user, result);
        if (result.hasErrors()) {
            List<CustomMessage> errors = new ArrayList<>();
            result.getFieldErrors().stream()
                    .forEach(action -> errors.add(new CustomMessage(action.getDefaultMessage())));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
        try {
            User u = userDao.register(user);
            
            // Generating token from User object
            final String token = jwtTokenUtil.generateToken(u);
            return new ResponseEntity<>(new JWTTokenResponse(token,u), HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }
    
    
    
     // User registeration
    @RequestMapping(value = "/auth/changepass",
            method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Object> changePassword(@RequestBody Password password, @RequestHeader HttpHeaders headers) throws Exception {
        
        try {
            String pass = password.getPassword();
            System.out.println("com.cinema.controller.UserController.changePassword() -- password " + pass);
         String token = headers.get("Authorization").get(0);
                String userId = jwtTokenUtil.getUserIdFromToken(token.substring(7));
        User user = userDao.changePassword(userId,pass);
        if( user != null) return new ResponseEntity<>(user , HttpStatus.OK);
        else {List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage("Unable to update password."));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);}
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }
    

    // Get user details along with following details in term of requesting user
    @RequestMapping(value = "/users/{userId}",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<Object> getUser(@PathVariable String userId, @RequestHeader HttpHeaders headers) throws Exception {
        try {
            String requestingUser = "";
            if (headers.get("Authorization") == null) {
                requestingUser += "guest";
            } else {
                String token = headers.get("Authorization").get(0);
                requestingUser += jwtTokenUtil.getUserIdFromToken(token.substring(7));
            }
            
            User user = userDao.getUser(userId);
            JSONObject userJson = new JSONObject();
            userJson.put("userId", user.getId());
            userJson.put("userName", user.getUserName());
            userJson.put("firstName", user.getFirstName());
            userJson.put("lastName", user.getLastName());
            userJson.put("userRole", user.getUserRole());
            
            if (!requestingUser.equals("guest")) {
                User reqUser = userDao.getUser(requestingUser);
                if (reqUser.getFollowings().contains(user)) {
                    userJson.put("isFollowing", true);
                } else {
                    userJson.put("isFollowing", false);
                }
            } else {
                userJson.put("isFollowing", false);
            }
            return new ResponseEntity<>(userJson.toMap(), HttpStatus.OK);
        } catch (Exception e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }

    }

    // Search Users
    @RequestMapping(value = "/users/search/{userName}",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<Object> searchUsers(@PathVariable String userName) throws Exception {
        try {
            List<User> users = userDao.search(userName);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch(UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }

    // Follow users
    // Access: private
    @RequestMapping(value = "/users/follow/{followingId}",
            method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Object> follow(@PathVariable String followingId, HttpServletRequest request) throws Exception {
        try {
            User user = (User) request.getAttribute("user");
            userDao.followThatUser(user, followingId);

            return new ResponseEntity<>(new CustomMessage("Followed Successfully"), HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }

    // Unfollow users
    // Access: private
    @RequestMapping(value = "/users/unfollow/{unfollowingId}",
            method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Object> unfollow(@PathVariable String unfollowingId, HttpServletRequest request) throws Exception {
        try {
            User user = (User) request.getAttribute("user");
            
            userDao.unfollowThatUser(user, unfollowingId);
            
            return new ResponseEntity<>(new CustomMessage("Unfollowed Successfully"), HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            System.out.println("UNFOLLOW1"+e.getMessage());
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }

    // Get followings list
    @RequestMapping(value = "/users/followings/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<Object> getFollowings(@PathVariable String id, @RequestHeader HttpHeaders headers) throws Exception {
        try {
            String requestingUser = "";
            if (headers.get("Authorization") == null) {
                requestingUser += "guest";
            } else {
                String token = headers.get("Authorization").get(0);
                requestingUser += jwtTokenUtil.getUserIdFromToken(token.substring(7));
            }
            
            Set<User> followings = userDao.getFollowings(id);
            
            User reqUser = null;
            if (!requestingUser.equals("guest")) {
                reqUser = userDao.getUser(requestingUser);
            }
            
            
            List<Map<String, Object>> list = new ArrayList<>();
            for(User u: followings) {
                JSONObject userJson = new JSONObject();
                userJson.put("userId", u.getId());
                userJson.put("userName", u.getUserName());
                userJson.put("firstName", u.getFirstName());
                userJson.put("lastName", u.getLastName());
                userJson.put("userRole", u.getUserRole());
                
                if(!requestingUser.equals("guest")) {
                    if(reqUser.getFollowings().contains(u))
                        userJson.put("isFollowing", true);
                    else
                        userJson.put("isFollowing", false);
                } else {
                    userJson.put("isFollowing", false);
                }
                
                list.add(userJson.toMap());
            }
            
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }

    // Get followers list
    @RequestMapping(value = "/users/followers/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<Object> getFollowers(@PathVariable String id, @RequestHeader HttpHeaders headers) throws Exception {
        try {
            String requestingUser = "";
            if (headers.get("Authorization") == null) {
                requestingUser += "guest";
            } else {
                String token = headers.get("Authorization").get(0);
                requestingUser += jwtTokenUtil.getUserIdFromToken(token.substring(7));
            }
            
            Set<User> followers = userDao.getFollowers(id);
            
            User reqUser = null;
            if (!requestingUser.equals("guest")) {
                reqUser = userDao.getUser(requestingUser);
            }
            
            
            List<Map<String, Object>> list = new ArrayList<>();
            for(User u: followers) {
                JSONObject userJson = new JSONObject();
                userJson.put("userId", u.getId());
                userJson.put("userName", u.getUserName());
                userJson.put("firstName", u.getFirstName());
                userJson.put("lastName", u.getLastName());
                userJson.put("userRole", u.getUserRole());
                
                if(!requestingUser.equals("guest")) {
                    if(reqUser.getFollowings().contains(u))
                        userJson.put("isFollowing", true);
                    else
                        userJson.put("isFollowing", false);
                } else {
                    userJson.put("isFollowing", false);
                }
                
                list.add(userJson.toMap());
            }
            
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }
    
    
}
