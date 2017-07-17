package com.greenfox;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.greenfox.model.Status;
import com.greenfox.repository.HeartbeatRepository;
import com.greenfox.service.MockRabbitService;
import com.greenfox.service.RabbitService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthenticationServiceApplication.class)
@WebAppConfiguration
@EnableWebMvc
@ActiveProfiles("test")
public class HeartbeatControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private HeartbeatRepository heartbeatRepository;

  @Autowired
  private RabbitService rabbitService;

  @Before
  public void setup() throws Exception {
    mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void getHeartbeatTest_DBOkAndQueueOk() throws Exception {
    DBSetupForOk();
    queueSetupForOk();
    mockMvc.perform(get("/heartbeat"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("ok"))
        .andExpect(jsonPath("$.database").value("ok"))
        .andExpect(jsonPath("$.queue").value("ok"));
  }

  @Test
  public void getHeartbeatTest_DBErrorAndQueueOk() throws Exception {
    DBSetupForError();
    queueSetupForOk();
    mockMvc.perform(get("/heartbeat"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("ok"))
        .andExpect(jsonPath("$.database").value("error"))
        .andExpect(jsonPath("$.queue").value("ok"));
  }

  @Test
  public void getHeartbeatTest_DBOkAndQueueError() throws Exception {
    DBSetupForOk();
    queueSetupForError();
    mockMvc.perform(get("/heartbeat"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("ok"))
        .andExpect(jsonPath("$.database").value("ok"))
        .andExpect(jsonPath("$.queue").value("error"));
  }

  @Test
  public void getHeartbeatTest_DBErrorAndQueueError() throws Exception {
    DBSetupForError();
    queueSetupForError();
    mockMvc.perform(get("/heartbeat"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("ok"))
        .andExpect(jsonPath("$.database").value("error"))
        .andExpect(jsonPath("$.queue").value("error"));
  }

  public void DBSetupForOk() {
    heartbeatRepository.deleteAll();
    Status status = new Status();
    status.setStatus(true);
    heartbeatRepository.save(status);
  }

  public void DBSetupForError() {
    heartbeatRepository.deleteAll();
  }

  public void queueSetupForOk() throws Exception {
    ((MockRabbitService) rabbitService).consume();
  }

  public void queueSetupForError() throws Exception {
    ((MockRabbitService) rabbitService).send();
  }
}
