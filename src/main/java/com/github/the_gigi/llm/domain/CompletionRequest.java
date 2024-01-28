package com.github.the_gigi.llm.domain;

import java.util.List;

/**
 * Represents a completion request for language models.
 */
public record CompletionRequest(
    /**
     * The base URL of the API.
     */
    String baseUrl,
    /**
     * The API key for authentication.
     */
    String apiKey,

    /**
     * The list of tools to be used.
     */
    List<Object> tools,
    /**
     * The maximum number of tokens in the generated output.
     */
    Integer maxTokens,
    /**
     * The temperature parameter for randomness in the output.
     */
    Double temperature,
    /**
     * The top-p parameter for controlling the diversity of the output.
     */
    Integer topP,
    /**
     * The number of completions to generate.
     */
    Integer n,
    /**
     * The model to use for completion.
     */
    String model,
    /**
     * The prompt text for completion.
     */
    String prompt
) {

  /**
   * Builder class for constructing instances of {@link CompletionRequest}.
   */
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

    /**
     * Sets the base URL of the API.
     *
     * @param baseUrl The base URL of the API.
     * @return The builder instance.
     */
    public Builder baseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }

    /**
     * Sets the API key for authentication.
     *
     * @param apiKey The API key for authentication.
     * @return The builder instance.
     */
    public Builder apiKey(String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    /**
     * Sets the list of tools to be used.
     *
     * @param tools The list of tools to be used.
     * @return The builder instance.
     */
    public Builder tools(List<Object> tools) {
      this.tools = tools;
      return this;
    }

    /**
     * Sets the maximum number of tokens in the generated output.
     *
     * @param maxTokens The maximum number of tokens in the generated output.
     * @return The builder instance.
     */
    public Builder maxTokens(Integer maxTokens) {
      this.maxTokens = maxTokens;
      return this;
    }

    /**
     * Sets the temperature parameter for randomness in the output.
     *
     * @param temperature The temperature parameter for randomness in the output.
     * @return The builder instance.
     */
    public Builder temperature(Double temperature) {
      this.temperature = temperature;
      return this;
    }

    /**
     * Sets the top-p parameter for controlling the diversity of the output.
     *
     * @param topP The top-p parameter for controlling the diversity of the output.
     * @return The builder instance.
     */
    public Builder topP(Integer topP) {
      this.topP = topP;
      return this;
    }

    /**
     * Sets the number of completions to generate.
     *
     * @param n The number of completions to generate.
     * @return The builder instance.
     */
    public Builder n(Integer n) {
      this.n = n;
      return this;
    }

    /**
     * Sets the model to use for completion.
     *
     * @param model The model to use for completion.
     * @return The builder instance.
     */
    public Builder model(String model) {
      this.model = model;
      return this;
    }

    /**
     * Sets the prompt text for completion.
     *
     * @param prompt The prompt text for completion.
     * @return The builder instance.
     */
    public Builder prompt(String prompt) {
      this.prompt = prompt;
      return this;
    }

    /**
     * Builds a new instance of {@link CompletionRequest} with the configured properties.
     *
     * @return A new instance of {@link CompletionRequest}.
     */
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

  /**
   * Creates a new instance of {@link Builder} to build {@link CompletionRequest} objects.
   *
   * @return A new instance of {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }
}
