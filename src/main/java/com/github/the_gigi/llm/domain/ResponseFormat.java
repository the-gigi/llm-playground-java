package com.github.the_gigi.llm.domain;

public enum ResponseFormat {
  TEXT("text"),
  JSON("json_object");

  private final String value;

  ResponseFormat(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
