package com.greenfox.service;

import com.greenfox.model.Heartbeat;
import com.greenfox.repository.HeartbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeartbeatService {

  private RabbitService rabbitService;
  private HeartbeatRepository heartbeatRepository;

  @Autowired
  public HeartbeatService(RabbitService rabbitService, HeartbeatRepository heartbeatRepository) {
    this.rabbitService = rabbitService;
    this.heartbeatRepository = heartbeatRepository;
  }

  public Heartbeat getHeartBeat() throws Exception {
    Heartbeat heartbeat = new Heartbeat("ok");
    if (heartbeatRepository.count() > 0) {
      heartbeat.setDatabase("ok");
    }
    if (rabbitService.rabbitMonitoring()) {
      heartbeat.setQueue("ok");
    }
    return heartbeat;
  }
}

