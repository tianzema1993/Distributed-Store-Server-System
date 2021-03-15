package com.example.Server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomePageController {
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String getHomePage() {
    return "Hello World";
  }
}
