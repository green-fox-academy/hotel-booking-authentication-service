package com.greenfox.service;

import com.greenfox.model.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtUnit {

  @Value("${secret}")
  private String secret;

//  public String createJwt(String issuer, String subject, long ttlMillis) throws Exception {
//
//    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//
//    long nowMillis = System.currentTimeMillis();
//    Date now = new Date(nowMillis);
//
//    JwtBuilder builder = Jwts.builder()
//        .setSubject(subject)
//        .setIssuedAt(now)
//        .setIssuer(issuer)
//        .signWith(
//            signatureAlgorithm,
//            "hotel-booking-authentication-service".getBytes("UTF-8")
//        );
//
//    if (ttlMillis >= 0) {
//      long expMillis = nowMillis + ttlMillis;
//      Date exp = new Date(expMillis);
//      builder.setExpiration(exp);
//    }
//
//    return builder.compact();
//  }

  public String generateToken(Account a) throws Exception {
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);
    Date exp = new Date(1800000);

    Claims claims = Jwts.claims().setSubject(a.getEmail());
//    claims.put("userId", a.getId() + "");
    claims.put("adminRole", a.isAdmin());

    return Jwts.builder()
        .setIssuer("Hotel-Booking-Authentication-Service")
        .setIssuedAt(now)
        .setExpiration(exp)
        .setClaims(claims)
        .signWith(SignatureAlgorithm.HS512, secret.getBytes("UTF-8"))
        .compact();
  }

  public Account parseToken(String token) throws Exception {
    try {
      Claims body = Jwts.parser()
          .setSigningKey(secret.getBytes("UTF-8"))
          .parseClaimsJws(token)
          .getBody();

      Account a = new Account();
      a.setEmail(body.getSubject());
//      a.setId(Long.parseLong((String) body.get("userId")));
      a.setAdmin((Boolean) body.get("adminRole"));

      return a;

    } catch (JwtException | ClassCastException e) {
      return null;
    }
  }
}
