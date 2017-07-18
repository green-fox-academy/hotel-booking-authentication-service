package com.greenfox.controller;

import com.greenfox.exception.InvalidPasswordException;
import com.greenfox.exception.NoSuchAccountException;
import com.greenfox.service.JwtAuthentication;
import com.greenfox.service.AuthService;
import com.greenfox.service.GsonService;
import com.greenfox.service.JwtUnit;
import com.greenfox.model.Account;
import com.greenfox.repository.AccountRepository;
import javax.servlet.http.HttpServletRequest;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class AuthController {

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

  @RequestMapping("/api/*")
  public ResponseEntity validate(HttpServletRequest request) {
    try {
      jwtAuthentication.attemptAuthentication(request);
      return new RestTemplate().postForEntity("hotel-booking-user-service.herokuapp.com/" + request.getRequestURI(), request, ResponseEntity.class);
    } catch (Exception e) {
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
