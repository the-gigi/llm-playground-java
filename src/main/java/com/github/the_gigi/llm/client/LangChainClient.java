package com.github.the_gigi.llm.client;


import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import com.github.the_gigi.llm.client.LLMClientBuilder.Provider;
import com.github.the_gigi.llm.client.LLMClientBuilder.Library;

interface Assistant {

  String chat(String userMessage);
}
public class LangChainClient implements LLMClient {

  Assistant defaultAssistant;
  String defaultBaseUrl;
  String defaultApiKey;
  String defaultModel;

  List<Object> defaultTtools;

  public static LLMClientBuilder builder(Provider provider, Library library) {
    return new LLMClientBuilder(provider, library);
  }
  public LangChainClient(String baseUrl, String apiKey, String model,
      @NotNull List<Object> tools) {

    this.defaultBaseUrl = baseUrl;
    this.defaultApiKey = apiKey;
    this.defaultModel = model;
    this.defaultTtools = tools;

    var client = OpenAiChatModel.builder()
        .apiKey(apiKey)
        .baseUrl(baseUrl)
        .modelName(model)
        .build();

    this.defaultAssistant = AiServices.builder(Assistant.class)
        .chatLanguageModel(client)
        .tools(tools)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .build();
  }

  @Override
  public String complete(String prompt) {
    return this.defaultAssistant.chat(prompt);

  }

  @Override
  public String complete(CompletionRequest r) {
    var client = OpenAiChatModel.builder()
        .apiKey(r.apiKey().isEmpty() ? this.defaultApiKey : r.apiKey())
        .baseUrl(r.baseUrl().isEmpty() ? this.defaultBaseUrl : r.baseUrl())
        .modelName(r.model().isEmpty() ? this.defaultModel : r.model())
        .build();
    var assistant = AiServices.builder(Assistant.class)
        .chatLanguageModel(client)
        .tools(r.tools().isEmpty() ? this.defaultTtools : r.tools())
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .build();

    return assistant.chat(r.prompt());
  }

  @Override
  public List<String> listModels() {
    return null;
  }
}