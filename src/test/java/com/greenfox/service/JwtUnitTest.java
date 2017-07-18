package com.greenfox.service;

import static org.junit.Assert.*;

import com.greenfox.model.Account;
import org.junit.Before;
import org.junit.Test;

public class JwtUnitTest {

  Account a = new Account();
  JwtUnit jwtUnit = new JwtUnit();

  @Before
  public void setup() {
    a.setAdmin(true);
    a.setEmail("dombo.peter@gmail.com");
  }

  @Test
  public void parseGeneratedToken() throws Exception {
  String token = jwtUnit.generateToken(a);
  Account a = jwtUnit.parseToken(token);
  assertTrue(a.isAdmin());
  assertEquals("dombo.peter@gmail.com", a.getEmail());
  }

  @Test
  public void parseToken_Verified() throws Exception {
    String validToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkb21iby5wZXRlckBnbWFpbC5jb20iLCJ1c2VySWQiOiIxIiwiYWRtaW5Sb2xlIjp0cnVlfQ.mKvGkiVCtGtJC3js3Zx5Rd5bwkURCV89p8h0IYZPsaUKnKyQD9qLuC7uciFVhBw373MklXYD_El66JBnJ3L5Pg";
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