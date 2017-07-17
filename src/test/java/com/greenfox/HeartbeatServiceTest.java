package com.greenfox;


import static org.junit.Assert.assertEquals;

import com.greenfox.model.Heartbeat;
import com.greenfox.repository.HeartbeatRepository;
import com.greenfox.service.HeartbeatService;
import com.greenfox.service.RabbitService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HeartbeatServiceTest {

  @InjectMocks
  private HeartbeatService heartbeatService;

  @Mock
  private HeartbeatRepository heartbeatRepository;
  @Mock
  private RabbitService rabbitService;

  @Test
  public void getHeartBeatForDatabaseErrorTest() throws Exception {
    Mockito.when(heartbeatRepository.count()).thenReturn(0L);
    assertEquals(heartbeatService.getHeartBeat().getDatabase(),
        new Heartbeat("ok").getDatabase());
  }

  @Test
  public void getHeartBeatForDatabaseOkTest() throws Exception {
    Mockito.when(heartbeatRepository.count()).thenReturn(1L);
    Heartbeat testHeartbeat = new Heartbeat("ok");
    testHeartbeat.setDatabase("ok");
    assertEquals(heartbeatService.getHeartBeat().getDatabase(),
        testHeartbeat.getDatabase());
  }

  @Test
  public void getHeartbeatForQueueErrorTest() throws Exception {
    assertEquals(heartbeatService.getHeartBeat().getQueue(),
        new Heartbeat("ok").getQueue());
  }

  @Test
  public void getHeartbeatForQueueOkTest() throws Exception {
    Heartbeat testHeartbeat = new Heartbeat("ok");
    testHeartbeat.setQueue("ok");
    Mockito.when(rabbitService.rabbitMonitoring()).thenReturn(true);
    assertEquals(heartbeatService.getHeartBeat().getQueue(),
        testHeartbeat.getQueue());
  }

  @After
  public void tearDown() throws Exception {
  }

}
