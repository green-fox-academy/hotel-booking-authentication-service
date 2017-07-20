package com.greenfox.service;

import com.greenfox.model.Account;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JwtUnitTest {

  Account a = new Account();
  JwtUnit jwtUnit = new JwtUnit();

  @Before
  public void setup() {

    a.setAdmin(true);
    a.setId(2L);
    a.setEmail("dombo.peter@gmail.com");
  }

  @Test
  public void parseGeneratedToken() throws Exception {
  String token = jwtUnit.generateToken(a);
  System.out.println(token);
  Account a = jwtUnit.parseToken(token);
    System.out.println(a.getId());
    System.out.println(a.getEmail());
    System.out.println(a.getToken());
    System.out.println(a.getPassword());
  assertTrue(a.isAdmin());
  assertEquals("dombo.peter@gmail.com", a.getEmail());
  }

  @Test
  public void parseToken_Verified() throws Exception {
    String validToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkb21iby5wZXRlckBnbWFpbC5jb20iLCJ1c2VySWQiOiIyIiwiYWRtaW5Sb2xlIjpmYWxzZX0.mYdVE_FMflTOzR8jA3zMP0E8SfM_LVoeBDvq6sV4C0uNCT7B3IVxK2bUbXr2szFF2uO6wEV_cuFqikb2fxwO2Q\n";
    Account a = jwtUnit.parseToken(validToken);
    assertTrue(a.isAdmin());
    assertEquals("dombo.peter@gmail.com", a.getEmail());
  }

  @Test
  public void parseToken_Invalid() throws Exception {
    String invalidToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJVc2VyQXV0aFNlcnZpY2UiLCJpYXQiOjE1MDAzNzMwMjAsImV4cCI6MTUzMTkwOTAxNiwiYXVkIjoid3d3LmdyZWVuZm94LmNvbSIsInN1YiI6ImRvbWJvLnBldGVAZ21haWwuY29tIiwidXNlcklkIjoiMSIsImFkbWluUm9sZSI6InRydWUifQ.bhFlOyznF2U1zb4bivPHe9BfoOiwhh05W5A-2VI0uuk";
    Account a = jwtUnit.parseToken(invalidToken);
    assertNull(a);
  }
}