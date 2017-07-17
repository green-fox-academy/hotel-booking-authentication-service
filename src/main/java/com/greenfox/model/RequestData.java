package com.greenfox.model;

public class RequestData {
  private Data data;

  public RequestData() {
  }

  public RequestData(Data data) {
    this.data = data;
  }

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }
}
