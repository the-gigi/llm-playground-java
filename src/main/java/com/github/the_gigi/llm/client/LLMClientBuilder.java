package com.github.the_gigi.llm.client;


import com.github.the_gigi.llm.domain.LLMClient;
import com.google.common.base.Strings;
import java.util.List;
import java.util.Optional;

public class LLMClientBuilder {

  private static final String OPENAI_BASE_URL = "https://api.openai.com/";
  private static final String ANYSCALE_BASE_URL = "https://api.endpoints.anyscale.com";

  private static final String LOCAL_BASE_URL = "http://localhost:5000";

  private static final String DEFAULT_ANYSCALE_MODEL = "mistralai/Mixtral-8x7B-Instruct-v0.1";

  private static final String DEFAULT_OPENAI_MODEL = "gpt-3.5-turbo-1106";

  public enum LLMProvider {
    OPEN_AI,
    ANYSCALE,
    LOCAL,
    CUSTOM
  }

  public enum LLMClientLibrary {
    OPENAI_JAVA,
    OPENAI_KOTLIN,
    LANG_CHAIN4J,
    SIMPLE_OPENAI
  }

  private final LLMProvider provider;

  private final LLMClientLibrary library;
  private String apiKey = "";
  private String baseUrl = "";
  private String model = "";

  private List<Object> tools;

  public LLMClientBuilder(LLMProvider provider, LLMClientLibrary library) {
    this.provider = provider;
    this.library = library;
  }

  public LLMClientBuilder apiKey(String apiKey) {
    this.apiKey = apiKey;
    return this;
  }

  public LLMClientBuilder baseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
    return this;
  }

  public LLMClientBuilder model(String model) {
    this.model = model;
    return this;
  }

  public LLMClientBuilder tools(List<Object> tools) {
    this.tools = tools;
    return this;
  }

  public LLMClient build() {
    // Set API key if not provided
    if (Strings.isNullOrEmpty(this.apiKey)) {
      switch (this.provider) {
        case OPEN_AI:
          this.apiKey = System.getenv("OPENAI_API_KEY");
          break;
        case ANYSCALE:
          this.apiKey = System.getenv("ANYSCALE_API_TOKEN");
          break;
        case CUSTOM:
          throw new IllegalArgumentException("API key must be provided for custom provider");
        default:
          throw new IllegalArgumentException("Unknown provider: " + provider);
      }
    }

    // Set API key if not provided
    if (Strings.isNullOrEmpty(this.model)) {
      switch (this.provider) {
        case OPEN_AI:
          this.model = DEFAULT_OPENAI_MODEL;
          break;
        case ANYSCALE:
          this.model = DEFAULT_ANYSCALE_MODEL;
          break;
        case CUSTOM:
          throw new IllegalArgumentException("Model must be provided for custom provider");
        default:
          throw new IllegalArgumentException("Unknown provider: " + provider);
      }
    }

    // Set base URL if not provided
    if (Strings.isNullOrEmpty(this.baseUrl)) {
      switch (this.provider) {
        case OPEN_AI:
          this.baseUrl = OPENAI_BASE_URL;
          if (this.library == LLMClientLibrary.LANG_CHAIN4J
              || this.library == LLMClientLibrary.OPENAI_KOTLIN) {
            this.baseUrl += "v1/";
          }
          break;
        case ANYSCALE:
          this.baseUrl = ANYSCALE_BASE_URL;
          if (this.library == LLMClientLibrary.LANG_CHAIN4J
              || this.library == LLMClientLibrary.OPENAI_KOTLIN) {
            this.baseUrl += "/v1/";
          }
          break;
        case CUSTOM:
          throw new IllegalArgumentException("Base URL must be provided for custom provider");
        default:
          throw new IllegalArgumentException("Unknown provider: " + provider);
      }
    }

    var tools = Optional.ofNullable(this.tools).orElse(List.of());
    return switch (this.library) {
      case OPENAI_JAVA -> new OpenAiJavaClient(this.baseUrl, this.apiKey, this.model, tools);
      case OPENAI_KOTLIN -> new OpenAiKotlinClient(this.baseUrl, this.apiKey, this.model, tools);
      case LANG_CHAIN4J -> new LangChainClient(this.baseUrl, this.apiKey, this.model, tools);
      case SIMPLE_OPENAI -> new SimpleOpenAiClient(this.baseUrl, this.apiKey, this.model, tools);
      default -> throw new IllegalArgumentException("Unknown library: " + library);
    };
  }
}
