package com.greenfox.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "heartbeat")
public class Status {

  @Column
  private Boolean status;
  @Id
  private int id;

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

}
