package com.springboot.springboot.service;

import org.springframework.stereotype.Service;

@Service
public class WendaService {
    public String getMessage(Integer userId){
        return  String.valueOf(userId);
    }
}
