/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinema.model;

/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */
public class Constants {
   
   enum Message {
    INVALID_USER  ("User is invalid"),
    INVALID_PASSWORD ("Password is invalid"),
    REQUIRED_USERNAME ("userName Required"),
           REQUIRED_PASSWORD ("password Required"),
          REQUIRED_FIRSTNAME  ("firstName Required"),
           REQUIRED_LASTNAME ("lastName Required"),
           REQUIRED_USERROLE("invalid userRole"),
           REQUIRED_EMAIL("Email is compulsary")
    ; // semicolon is needed when fields or methods to follow

    private final String dayInfo;

    Message(String dayInfo) {
        this.dayInfo = dayInfo;
    }
    
    public String getDayInfo() {
        return this.dayInfo;
    }    
    }
   
}
