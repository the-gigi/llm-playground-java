package com.github.the_gigi.llm.common;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonHelpers {
  public static  boolean isValidJson(String maybeJson) {
    try {
      var objectMapper = new ObjectMapper();
      objectMapper.readTree(maybeJson);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
