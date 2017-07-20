package com.greenfox.controller;

import com.greenfox.model.Account;
import com.greenfox.model.Heartbeat;
import com.greenfox.model.RequestData;
import com.greenfox.repository.AccountRepository;
import com.greenfox.service.AuthService;
import com.greenfox.service.GsonService;
import com.greenfox.service.JwtAuthentication;
import com.greenfox.service.JwtUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;


@RestController
public class AuthController {

  private String userRepoServiceDomain = System.getenv("userRepoServiceDomain");

  private AccountRepository accountRepository;
  private JwtUnit jwtUnit;
  private GsonService gsonService;
  private AuthService authService;
  private JwtAuthentication jwtAuthentication;

  private Account credentials;
  private Account responseAccount;
  private String response;

  @Autowired
  public AuthController(AccountRepository accountRepository, JwtUnit jwtUnit,
      GsonService gsonService, AuthService authService, JwtAuthentication jwtAuthentication) {
    this.jwtUnit = jwtUnit;
    this.accountRepository = accountRepository;
    this.gsonService = gsonService;
    this.authService = authService;
    this.jwtAuthentication = jwtAuthentication;
  }

  @GetMapping("/heartbeat")
  public ResponseEntity heartbeat(HttpServletRequest request) {
    try {
      jwtAuthentication.attemptAuthentication(request);
      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity response = restTemplate.getForEntity("http://" + userRepoServiceDomain + request.getRequestURI(), Heartbeat.class);
      return response;
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }


  @GetMapping("/api/users")
  public ResponseEntity getUsers(HttpServletRequest request) {
    try {
      jwtAuthentication.attemptAuthentication(request);
      RestTemplate restTemplate = new RestTemplate();
      return restTemplate.getForEntity("http://" + userRepoServiceDomain + request.getRequestURI(), Object.class);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  @GetMapping("/api/users/{userId}")
  public ResponseEntity getUser(HttpServletRequest request) {
    try {
      jwtAuthentication.attemptAuthentication(request);
      RestTemplate restTemplate = new RestTemplate();
      return restTemplate.getForEntity("http://" + userRepoServiceDomain + request.getRequestURI(), Object.class);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  @DeleteMapping("/api/users/{userId}")
  public ResponseEntity deleteUser(HttpServletRequest request) {
    try {
      jwtAuthentication.attemptAuthentication(request);
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.delete("http://" + userRepoServiceDomain + request.getRequestURI());
      return new ResponseEntity<>("{}",HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  @PutMapping("/api/users/{userId}")
  public ResponseEntity updateUser(HttpServletRequest request, @RequestBody RequestData requestData) {
    try {
      jwtAuthentication.attemptAuthentication(request);
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.put(
          "http://" + userRepoServiceDomain + request.getRequestURI(), requestData);
      return new ResponseEntity<>("{}",HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
  
  @PostMapping(value = "/register", produces = "application/json")
  public ResponseEntity saveAccount(HttpServletRequest request, @RequestBody RequestData requestData) throws Exception {
    try {
      jwtAuthentication.attemptAuthentication(request);
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.put(
          "http://" + userRepoServiceDomain + request.getRequestURI(), requestData);
      return new ResponseEntity<>("{}",HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping(value = "/login", produces = "application/json")
  public ResponseEntity authenticateAccount(HttpServletRequest request, @RequestBody RequestData requestData) throws Exception {
    try {
      jwtAuthentication.attemptAuthentication(request);
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.put(
          "http://" + userRepoServiceDomain + request.getRequestURI(), requestData);
      return new ResponseEntity<>("{}",HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
}
