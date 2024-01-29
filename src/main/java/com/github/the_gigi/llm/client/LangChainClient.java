package com.github.the_gigi.llm.client;

import com.github.the_gigi.llm.domain.CompletionRequest;
import com.github.the_gigi.llm.domain.LLMClient;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import java.util.List;
import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.client.LLMClientBuilder.LLMClientLibrary;
import java.util.Optional;

/**
 * Interface defining the assistant's chat functionality.
 */
interface Assistant {
  /**
   * Handles chat interactions.
   *
   * @param userMessage The message from the user.
   * @return The response from the assistant.
   */
  String chat(String userMessage);
}

/**
 * Client class for interacting with LangChain services.
 */
public class LangChainClient implements LLMClient {

  private final Assistant defaultAssistant;
  private final String defaultBaseUrl;
  private final String defaultApiKey;
  private final String defaultModel;
  private final List<Object> defaultTools;

  /**
   * Factory method to create a new LLMClientBuilder configured with LangChain4J.
   *
   * @param provider The provider of the LangChain client.
   * @return A new instance of LLMClientBuilder configured with LangChain4J library.
   */
  public static LLMClientBuilder builder(LLMProvider provider) {
    return new LLMClientBuilder(provider, LLMClientLibrary.LANG_CHAIN4J);
  }

  /**
   * Constructs a new LangChainClient with specified parameters.
   *
   * @param baseUrl The base URL for the LangChain service.
   * @param apiKey  The API key for authentication.
   * @param model   The model name for language processing.
   * @param tools   A list of tools used in the client.
   */
  public LangChainClient(String baseUrl, String apiKey, String model, List<Object> tools) {
    tools = Optional.ofNullable(tools).orElse(List.of());

    this.defaultBaseUrl = baseUrl;
    this.defaultApiKey = apiKey;
    this.defaultModel = model;
    this.defaultTools = tools;

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

  /**
   * Completes a chat request using a given prompt.
   *
   * @param prompt The input string to be processed.
   * @return The response string from the assistant.
   */
  @Override
  public String complete(String prompt) {
    return this.defaultAssistant.chat(prompt);
  }

  /**
   * Completes a chat request based on a CompletionRequest object.
   *
   * @param r The CompletionRequest containing details for the request.
   * @return The response string from the assistant.
   */
  @Override
  public String complete(CompletionRequest r) {
    var client = OpenAiChatModel.builder()
        .apiKey(Optional.ofNullable(r.apiKey()).orElse(this.defaultApiKey))
        .baseUrl(Optional.ofNullable(r.baseUrl()).orElse(this.defaultBaseUrl))
        .modelName(Optional.ofNullable(r.model()).orElse(this.defaultModel))
        .build();
    var assistant = AiServices.builder(Assistant.class)
        .chatLanguageModel(client)
        .tools(Optional.ofNullable(r.tools()).orElse(this.defaultTools))
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .build();

    return assistant.chat(r.prompt());
  }

  /**
   * Lists available models.
   *
   * @return A list of available model names.
   */
  @Override
  public List<String> listModels() {
    return List.of();
  }
}
