package com.greenfox.controller;

import com.greenfox.exception.InvalidPasswordException;
import com.greenfox.exception.NoSuchAccountException;
import com.greenfox.service.AuthService;
import com.greenfox.service.GsonService;
import com.greenfox.service.JwtCreator;
import com.greenfox.model.Account;
import com.greenfox.repository.AccountRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

  private AccountRepository accountRepository;
  private JwtCreator jwtCreator;
  private GsonService gsonService;
  private AuthService authService;

  private Account credentials;
  private String jwt;
  private String pw_hashed;
  private Account responseAccount;
  private String response;

  @Autowired
  public AuthController(AccountRepository accountRepository, JwtCreator jwtCreator,
      GsonService gsonService, AuthService authService) {
    this.jwtCreator = jwtCreator;
    this.accountRepository = accountRepository;
    this.gsonService = gsonService;
    this.authService = authService;
  }

  @PostMapping(value = "/register", produces = "application/json")
  public ResponseEntity saveAccount(@RequestBody String json) throws Exception {
    credentials = getCredentials(json);
    if (isRegisteredUser()) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    } else {
      jwt = createJwtForNewUser();
      pw_hashed = hashPassword(credentials.getPassword());
      accountRepository.save(new Account(credentials.getEmail(), false, jwt, pw_hashed));
      response = createResponse(credentials.getEmail());
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
  }

  @PostMapping(value = "/login", produces = "application/json")
  public ResponseEntity authenticateAccount(@RequestBody String json) throws Exception {

    credentials = getCredentials(json);

    try {
      authenticateUser(credentials.getEmail(), credentials.getPassword());
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

  public String createJwtForNewUser() throws Exception {
    return jwtCreator.createJwt("hotel-booking-user-service", "new user", 300000);
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

  public void authenticateUser(String email, String password) throws Exception {
    authService.authenticate(email, password);
  }
}
