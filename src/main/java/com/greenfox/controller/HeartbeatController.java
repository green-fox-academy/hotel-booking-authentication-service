//package com.greenfox.controller;
//
//
//import com.greenfox.model.Consume;
//import com.greenfox.model.Heartbeat;
//import com.greenfox.model.Send;
//import com.greenfox.service.HeartbeatService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class HeartbeatController {
//
//  private Send send = new Send();
//  private Consume consume = new Consume();
//  private HeartbeatService heartbeatService;
//
//  @Autowired
//  public HeartbeatController(HeartbeatService heartbeatService) {
//    this.heartbeatService = heartbeatService;
//  }
//
//  @GetMapping("/heartbeat")
//  public Heartbeat validateMessage() throws Exception {
//    return heartbeatService.getHeartBeat();
//  }
//
//  @GetMapping("/sendevent")
//  public void sendEvent() throws Exception {
//    send.dispatch("user-service.herokuapp.com", "hello");
//    consume.consume("events");
//  }
//}
