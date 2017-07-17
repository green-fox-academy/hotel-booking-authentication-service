package com.greenfox.model;

import com.greenfox.model.Links;

public class Data {
  private Links links;
  private String type;
  private Object attributes;

  public Data() {
  }

  public Data(Links links, String type, Object attributes) {
    this.links = links;
    this.type = type;
    this.attributes = attributes;
  }

  public Data(String type, Object attributes) {
    this.type = type;
    this.attributes = attributes;
  }

  public Links getLinks() {
    return links;
  }

  public void setLinks(Links links) {
    this.links = links;
  }

  public String getType() {
    return type;
  }

  public Object getAttributes() {
    return attributes;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setAttributes(Account attributes) {
    this.attributes = attributes;
  }
}
