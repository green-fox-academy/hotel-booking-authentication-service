package com.greenfox.model;

import com.greenfox.exception.JwtTokenMalformedException;
import com.greenfox.exception.JwtTokenMissingException;
import com.greenfox.service.JwtUnit;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

public class JwtAuthentication {

  @Autowired
  JwtUnit jwtUnit;

  public Account attemptAuthentication(HttpServletRequest request) throws Exception {
    String header = request.getHeader("Authorization");

    if (header == null || !header.startsWith("Bearer ")) {
      throw new JwtTokenMissingException("No JWT token found in request headers");
    }

    String authToken = header.substring(7);

    return authenticate(authToken);
  }

  public Account authenticate(String authToken) throws Exception {
    Account parsedUser = jwtUnit.parseToken(authToken);

    if (parsedUser == null) {
      throw new JwtTokenMalformedException("JWT token is not valid");
    }

    return parsedUser;
  }

}
