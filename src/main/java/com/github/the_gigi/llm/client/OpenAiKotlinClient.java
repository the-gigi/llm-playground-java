package com.github.the_gigi.llm.client;

import static com.github.the_gigi.llm.client.OpenAiClientHelperKt.createOpenAiKotlinClient;
import static com.github.the_gigi.llm.client.OpenAiClientHelperKt.getCompletionResponse;
import static com.github.the_gigi.llm.client.OpenAiClientHelperKt.getModels;

import com.aallam.openai.client.OpenAI;
import com.github.the_gigi.llm.client.LLMClientBuilder.Library;
import com.github.the_gigi.llm.client.LLMClientBuilder.Provider;
import java.util.List;

public class OpenAiKotlinClient implements LLMClient {

  private final OpenAI client;

  private final String model;

  private final List<FunctionToolCallData> tools;

  public static LLMClientBuilder builder(Provider provider, Library library) {
    return new LLMClientBuilder(provider, library);
  }

  public OpenAiKotlinClient(String baseUrl,
      String apiKey,
      String model,
      List<Object> tools)
  {
    this.client = createOpenAiKotlinClient(baseUrl, apiKey);
    this.model = model;
    if (tools == null) {
      this.tools = List.of();
    } else {
      this.tools = tools.stream().map(t -> (FunctionToolCallData) t).toList();
    }
  }

  @Override
  public String complete(String prompt) {
    var r = CompletionRequest.builder()
        .model(this.model)
        .prompt(prompt)
        .tools(this.tools.stream().map(t -> (Object) t).toList())
        .build();

    // Return the first result
    return this.complete(r);
  }

  @Override
  public String complete(CompletionRequest r) {
    var tools = this.tools.stream().map(t -> (FunctionToolCallData) t).toList();
    var result = getCompletionResponse(this.client, List.of("user," + r.prompt()), r.model(), tools);

    // Return the first result
    return result.getContent();
  }

  @Override
  public List<String> listModels() {
    try {
      return getModels(this.client);
    } catch (Exception e) {
      return List.of();
    }
  }
}
