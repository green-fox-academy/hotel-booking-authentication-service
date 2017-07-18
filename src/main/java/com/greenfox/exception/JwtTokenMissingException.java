package com.greenfox.exception;

public class JwtTokenMissingException extends Exception {

  public JwtTokenMissingException() {
  }

  public JwtTokenMissingException(String message) {
    super(message);
  }
}
