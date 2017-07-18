package com.greenfox.exception;

public class JwtTokenMalformedException extends Exception {

  public JwtTokenMalformedException() {
  }

  public JwtTokenMalformedException(String message) {
    super(message);
  }
}
