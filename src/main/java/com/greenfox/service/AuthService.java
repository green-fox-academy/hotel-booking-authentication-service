package com.greenfox.service;

import com.greenfox.exception.InvalidPasswordException;
import com.greenfox.exception.NoSuchAccountException;
import com.greenfox.model.Account;
import com.greenfox.repository.AccountRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  AccountRepository accountRepository;

  public void authenticate(String email, String password) throws Exception {
    Account account;
    if (!checkAccount(email)) {
      throw new NoSuchAccountException("Invalid email");
    } else {
      account = accountRepository.findAccountByEmail(email);
    }
    if (!checkPassword(password, account.getPassword())) {
      throw new InvalidPasswordException("Invalid password");
    };
  }

  public boolean checkAccount(String email) {
    return (accountRepository.findAccountByEmail(email) != null);
  }

  public boolean checkPassword(String password, String pw_hashed) {
    return BCrypt.checkpw(password, pw_hashed);
  }
}
