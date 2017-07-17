package com.greenfox.service;

import com.greenfox.model.Consume;
import com.greenfox.model.Send;
import org.springframework.stereotype.Service;

@Service
public class RabbitService {

  public RabbitService() {
  }

  public boolean rabbitMonitoring() throws Exception {
    Send send = new Send();
    Consume consume = new Consume();
    send.send("monitoring message");
    consume.consume("heartbeat");
    String received = consume.getReceivedMessage();
    return (received.equals("monitoring message"));
  }
}
