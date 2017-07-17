package com.greenfox.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeoutException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("default")
public class ConnectionSetter {

  private static URI rabbitmqUrl;
  ConnectionFactory factory;
  Connection connection;
  Channel channel;

  public ConnectionSetter() throws IOException, TimeoutException {
    try {
      rabbitmqUrl = new URI(System.getenv("RABBITMQ_BIGWIG_URL"));
    } catch (URISyntaxException e) {
      e.getStackTrace();
    }
    this.factory = new ConnectionFactory();
    factory.setUsername(rabbitmqUrl.getUserInfo().split(":")[0]);
    factory.setPassword(rabbitmqUrl.getUserInfo().split(":")[1]);
    factory.setHost(rabbitmqUrl.getHost());
    factory.setPort(rabbitmqUrl.getPort());
    factory.setVirtualHost(rabbitmqUrl.getPath().substring(1));
    this.connection = factory.newConnection();
    this.channel = connection.createChannel();
  }

  public ConnectionFactory getFactory() {
    return factory;
  }

  public Connection getConnection() {
    return connection;
  }

  public Channel getChannel() {
    return channel;
  }

}
