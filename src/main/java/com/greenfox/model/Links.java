package com.greenfox.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Links {

  private String self;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String next;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String prev;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String last;

  public String getSelf() {
    return self;
  }

  public void setSelf(String self) {
    this.self = self;
  }

  public String getNext() {
    return next;
  }

  public void setNext(String next) {
    this.next = next;
  }

  public String getPrev() {
    return prev;
  }

  public void setPrev(String prev) {
    this.prev = prev;
  }

  public String getLast() {
    return last;
  }

  public void setLast(String last) {
    this.last = last;
  }
}

