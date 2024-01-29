package com.github.the_gigi.openai.client;

import static com.theokanning.openai.service.OpenAiService.defaultClient;
import static com.theokanning.openai.service.OpenAiService.defaultObjectMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.service.OpenAiService;
import java.time.Duration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class OpenAiJavaClientBuilder {

  private static final String DEFAULT_BASE_URL = "https://api.openai.com/";
  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
  private static final ObjectMapper mapper = defaultObjectMapper();

  private String base_url = DEFAULT_BASE_URL;
  private final String token;
  private Duration timeout = DEFAULT_TIMEOUT;
  private String defaultModel;

  public OpenAiJavaClientBuilder(String token) {
    this.token = token;
  }

  public OpenAiJavaClientBuilder baseUrl(String base_url) {
    this.base_url = base_url;
    return this;
  }

  public OpenAiJavaClientBuilder timeout(Duration timeout) {
    this.timeout = timeout;
    return this;
  }

  public OpenAiJavaClientBuilder defaultModel(String model) {
    this.defaultModel = model;
    return this;
  }


  public OpenAiJavaClient build() {
    var client = defaultClient(token, timeout);
    var retrofit = new Retrofit.Builder()
        .baseUrl(base_url)
        .client(client)
        .addConverterFactory(JacksonConverterFactory.create(mapper))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();

    var api = retrofit.create(OpenAiApi.class);
    var service = new OpenAiService(api);
    return new OpenAiJavaClientImpl(service, this.defaultModel);
  }
}
