/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinema.Exceptions;

import com.cinema.model.CustomMessage;
import java.util.List;

/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */
public class ErrorsResponse {
    
    List<CustomMessage> messages ;
    
      public ErrorsResponse(List<CustomMessage> list) {
        this.messages = list;
    }

    public List<CustomMessage> getError() {
        return messages;
    }

    public void setError(List<CustomMessage> list) {
        this.messages = list;
    }
}
