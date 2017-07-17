package com.greenfox.exception;

public class NoSuchAccountException extends Exception {

  public NoSuchAccountException() {
  }

  public NoSuchAccountException(String message) {
    super(message);
  }
}
