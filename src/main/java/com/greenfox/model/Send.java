package com.greenfox.model;

import com.google.gson.Gson;
import com.greenfox.service.ConnectionSetter;

public class Send {

  public Send() {
  }

  public void send(String message) throws Exception {
    ConnectionSetter connectionSetter = new ConnectionSetter();
    connectionSetter.getChannel().basicPublish("", "heartbeat", null, message.getBytes("UTF-8"));
    System.out.println("Message sent: '" + message + "'");
  }

  public String dispatch(String hostname, String message) throws Exception {
    ConnectionSetter connectionSetter = new ConnectionSetter();
    Gson gson = new Gson();
    String eventJson = gson.toJson(new Event(hostname, message));
    connectionSetter.getChannel().basicPublish("", "events", null, eventJson.getBytes());
    return eventJson;
  }

}
