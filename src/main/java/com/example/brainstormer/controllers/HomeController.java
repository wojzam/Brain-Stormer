package com.example.brainstormer.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    @ResponseBody
    public ResponseEntity<String> home() {
        return new ResponseEntity<>("Hello world!", HttpStatus.OK);
    }

}
