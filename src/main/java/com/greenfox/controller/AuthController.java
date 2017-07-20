package com.greenfox.controller;

import com.greenfox.exception.InvalidPasswordException;
import com.greenfox.exception.NoSuchAccountException;
import com.greenfox.model.Account;
import com.greenfox.model.Heartbeat;
import com.greenfox.model.RequestData;
import com.greenfox.repository.AccountRepository;
import com.greenfox.service.AuthService;
import com.greenfox.service.GsonService;
import com.greenfox.service.JwtAuthentication;
import com.greenfox.service.JwtUnit;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
  public ResponseEntity deleteUser(@PathVariable int userId, HttpServletRequest request) {
    try {
      jwtAuthentication.attemptAuthentication(request);
      String authenticationHeaderContent = request.getHeader("Authorization").substring(7);
      System.out.println("Auth header: " + authenticationHeaderContent);
      Account currentAccount = jwtUnit.parseToken(authenticationHeaderContent);
      System.out.println("Account done");
      if (!currentAccount.isAdmin() && currentAccount.getId() != userId) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      } else {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete("http://" + userRepoServiceDomain + request.getRequestURI());
        return new ResponseEntity<>("{}", HttpStatus.OK);
      }
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
      return new ResponseEntity<>("{}", HttpStatus.OK);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping(value = "/register", produces = "application/json")
  public ResponseEntity saveAccount(@RequestBody String json) throws Exception {
    credentials = getCredentials(json);
    if (isRegisteredUser()) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    } else {
      Account account = new Account();
      account.setPassword(hashPassword(credentials.getPassword()));
      account.setEmail(credentials.getEmail());
      account.setToken(createJwt(account));
      accountRepository.save(account);
      response = createResponse(credentials.getEmail());
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
  }

  @PostMapping(value = "/login", produces = "application/json")
  public ResponseEntity authenticateAccount(@RequestBody String json) throws Exception {

    credentials = getCredentials(json);

    try {
      Account account = authenticateUser(credentials.getEmail(), credentials.getPassword());
      System.out.println(account.getToken());
      account.setToken(createJwt(account));
      accountRepository.save(account);
      response = createResponse(credentials.getEmail());
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (NoSuchAccountException | InvalidPasswordException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  public Account getCredentials(String json) {
    return gsonService.parseCredentials(json);
  }

  public boolean isRegisteredUser() {
    return authService.checkAccount(credentials.getEmail());
  }

  public String createJwt(Account a) throws Exception {
    return jwtUnit.generateToken(a);
  }

  public String hashPassword(String password) {
    return BCrypt
            .hashpw(password, BCrypt.gensalt((Integer.parseInt(System.getenv("LOG_ROUNDS")))));
  }

  public String createResponse(String email) {
    responseAccount = accountRepository.findAccountByEmail(email);
    return gsonService.createAccountJson(responseAccount.getId(), responseAccount.getEmail(),
            responseAccount.isAdmin(), responseAccount.getToken());
  }

  public Account authenticateUser(String email, String password) throws Exception {
    return authService.authenticate(email, password);
  }
}
