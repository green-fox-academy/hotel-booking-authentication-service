package com.greenfox.controller;

import com.greenfox.model.Account;
import com.greenfox.service.JwtAuthentication;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthController {

  private String userRepoServiceDomain = System.getenv("userRepoServiceDomain");
  private JwtAuthentication jwtAuthentication;

  @Autowired
  public AuthController(JwtAuthentication jwtAuthentication) {
    this.jwtAuthentication = jwtAuthentication;
  }

  @RequestMapping({"/api/users","/api/users/{userId}"})
  public ResponseEntity crud(HttpServletRequest servletRequest, HttpEntity entityRequest, @PathVariable(required = false) Long userId) {
    try {
      Account account = jwtAuthentication.attemptAuthentication(servletRequest);
      if (!account.isAdmin() && account.getId() != userId) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      } else {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(URI.create("http://" + userRepoServiceDomain + servletRequest.getRequestURI()), HttpMethod.valueOf(servletRequest.getMethod()),
            entityRequest, Object.class);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping(value = {"/register", "/login"}, produces = "application/json")
  public ResponseEntity saveAccount(HttpServletRequest request, @RequestBody String json) throws Exception {
      return new RestTemplate().postForEntity(
          "http://" + userRepoServiceDomain + request.getRequestURI(), json, Object.class);
  }
}
