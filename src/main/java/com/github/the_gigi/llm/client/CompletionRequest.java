package com.github.the_gigi.llm.client;

import java.util.List;

public record CompletionRequest(
    String baseUrl,
    String apiKey,

    List<Object> tools,
    Integer maxTokens,
    Double temperature,
    Integer topP,
    Integer n,
    String model,
    String prompt
) {
  public static class Builder {
    private String baseUrl;
    private String apiKey;

    private List<Object> tools = List.of();
    private Integer maxTokens;
    private Double temperature;
    private Integer topP;
    private Integer n;
    private String model;
    private String prompt;

    public Builder baseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }

    public Builder apiKey(String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    public Builder tools(List<Object> tools) {
      this.tools = tools;
      return this;
    }

    public Builder maxTokens(Integer maxTokens) {
      this.maxTokens = maxTokens;
      return this;
    }

    public Builder temperature(Double temperature) {
      this.temperature = temperature;
      return this;
    }

    public Builder topP(Integer topP) {
      this.topP = topP;
      return this;
    }

    public Builder n(Integer n) {
      this.n = n;
      return this;
    }

    public Builder model(String model) {
      this.model = model;
      return this;
    }

    public Builder prompt(String prompt) {
      this.prompt = prompt;
      return this;
    }

    public CompletionRequest build() {
      return new CompletionRequest(
          baseUrl,
          apiKey,
          tools,
          maxTokens,
          temperature,
          topP,
          n,
          model,
          prompt);
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}

