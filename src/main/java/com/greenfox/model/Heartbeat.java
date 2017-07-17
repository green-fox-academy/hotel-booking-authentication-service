package com.greenfox.model;

public class Heartbeat {

  private String status;
  private String database;
  private String queue;

  public Heartbeat() {
  }

  public Heartbeat(String status) {
    this.status = status;
    database = "error";
    queue = "error";
  }

  public String getStatus() {
    return status;
  }

  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  public String getQueue() {
    return queue;
  }

  public void setQueue(String queue) {
    this.queue = queue;
  }
}
