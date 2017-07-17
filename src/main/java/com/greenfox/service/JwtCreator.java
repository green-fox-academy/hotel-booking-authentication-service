package com.greenfox.service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class JwtCreator {

  public String createJwt(String issuer, String subject, long ttlMillis) throws Exception {

    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);

    JwtBuilder builder = Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(now)
        .setIssuer(issuer)
        .signWith(
            signatureAlgorithm,
            "secret".getBytes("UTF-8")
        );

    if (ttlMillis >= 0) {
      long expMillis = nowMillis + ttlMillis;
      Date exp = new Date(expMillis);
      builder.setExpiration(exp);
    }

    return builder.compact();
  }
}
