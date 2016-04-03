package com.example.supernova.pfe.exceptions;

import android.content.res.Resources;


public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String msg){
        super(msg);
    }
}
