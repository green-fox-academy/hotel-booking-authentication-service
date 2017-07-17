package com.greenfox.model;

import com.greenfox.service.ConnectionSetter;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;

public class Consume {
  public String receivedMessage;

  public Consume() {
    this.receivedMessage = "";
  }

  public void consume(String queue) throws Exception {
    ConnectionSetter connectionSetter = new ConnectionSetter();
    connectionSetter.getChannel().queueDeclare(queue, false, false, false, null);

    Consumer consumer = new DefaultConsumer(connectionSetter.getChannel()) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
          AMQP.BasicProperties properties, byte[] body)
          throws IOException {
        String message = new String(body, "UTF-8");
        receivedMessage = message;
        System.out.println("Received '" + message + "'");
      }
    };

    connectionSetter.getChannel().basicConsume(queue, true, consumer);
    connectionSetter.getChannel().close();
    connectionSetter.getConnection().close();
  }

  public String getReceivedMessage() {
    return receivedMessage;
  }
}
