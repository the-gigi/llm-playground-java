package com.github.the_gigi.llm.client;


import java.util.List;

public class LLMClientBuilder {

  private static final String OPENAI_BASE_URL = "https://api.openai.com/";
  private static final String ANYSCALE_BASE_URL = "https://api.endpoints.anyscale.com";

  private static final String LOCAL_BASE_URL = "http://localhost:5000";

  private static final String DEFAULT_ANYSCALE_MODEL = "mistralai/Mixtral-8x7B-Instruct-v0.1";

  private static final String DEFAULT_OPENAI_MODEL = "gpt-3.5-turbo";

  public enum Provider {
    OPEN_AI,
    ANYSCALE,

    LOCAL
  }

  public enum Library {
    OPENAI_JAVA,
    OPENAI_KOTLIN,
    LANG_CHAIN4J,
    SIMPLE_OPENAI
  }

  private final Provider provider;

  private final Library library;
  private String apiKey = "";
  private String baseUrl = "";
  private String model = "";

  private List<Object> tools;

  public LLMClientBuilder (Provider provider, Library library) {
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
    if (this.apiKey.isEmpty()) {
      switch (this.provider) {
        case OPEN_AI:
          this.apiKey = System.getenv("OPENAI_API_KEY");
          break;
        case ANYSCALE:
          this.apiKey = System.getenv("ANYSCALE_API_TOKEN");
          break;
        default:
          throw new IllegalArgumentException("Unknown provider: " + provider);
      }
    }

    // Set API key if not provided
    if (this.model.isEmpty()) {
      switch (this.provider) {
        case OPEN_AI:
          this.model = DEFAULT_OPENAI_MODEL;
          break;
        case ANYSCALE:
          this.model = DEFAULT_ANYSCALE_MODEL;
          break;
        default:
          throw new IllegalArgumentException("Unknown provider: " + provider);
      }
    }

    // Set base URL if not provided
    if (this.baseUrl.isEmpty()) {
      switch (this.provider) {
        case OPEN_AI:
          this.baseUrl = OPENAI_BASE_URL;
          if (this.library == Library.LANG_CHAIN4J) {
            this.baseUrl += "v1/";
          }
          break;
        case ANYSCALE:
          this.baseUrl = ANYSCALE_BASE_URL;
          if (this.library == Library.LANG_CHAIN4J) {
            this.baseUrl += "/v1/";
          }
          break;
        default:
          throw new IllegalArgumentException("Unknown provider: " + provider);
      }
    }

    switch (this.library) {
//      case OPENAI_JAVA:
//        return new OpenAiJavaClient(this.baseUrl, this.apiKey, this.model);
//      case OPENAI_KOTLIN:
//        return new OpenAiKotlinClient(this.baseUrl, this.apiKey, this.model);
      case LANG_CHAIN4J:
        return new LangChainClient(this.baseUrl, this.apiKey, this.model, this.tools);
      case SIMPLE_OPENAI:
        return new SimpleOpenAiClient(this.baseUrl, this.apiKey, this.model, this.tools);
      default:
        throw new IllegalArgumentException("Unknown library: " + library);
    }
  }
}
