package com.github.the_gigi.llm.client;


import com.github.the_gigi.llm.client.LLMClientBuilder.LLMClientLibrary;
import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.domain.CompletionRequest;
import com.github.the_gigi.llm.domain.LLMClient;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.message.ChatMsg;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgResponse;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgTool;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgUser;
import io.github.sashirestela.openai.domain.chat.tool.ChatFunction;
import io.github.sashirestela.openai.domain.model.ModelResponse;
import io.github.sashirestela.openai.function.FunctionExecutor;
import io.github.sashirestela.openai.function.Functional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class SimpleOpenAiClient implements LLMClient {

  public record FunctionInfo(String name, String description,
                             Class<? extends Functional> funcClass) {

  }

  private final SimpleOpenAI openai;

  private final String model;
  private final List<FunctionInfo> functions;

  public static LLMClientBuilder builder(LLMProvider provider) {
    return new LLMClientBuilder(provider, LLMClientLibrary.SIMPLE_OPENAI);
  }

  public SimpleOpenAiClient(
      String baseUrl,
      String apiKey,
      String model,
      List<Object> tools) {
    this.openai = SimpleOpenAI.builder()
        .baseUrl(baseUrl)
        .apiKey(apiKey)
        .build();
    this.model = model;

    if (tools == null) {
      this.functions = List.of();
    } else {
      this.functions = tools.stream().map(t -> (FunctionInfo) t).toList();
    }
  }

  @Override
  public String complete(String prompt) {
    return this.complete(CompletionRequest.builder().prompt(prompt).build());
  }

  @Override
  public String complete(CompletionRequest r) {
    var functionExecutor = new FunctionExecutor();
    this.functions.forEach(f -> functionExecutor.enrollFunction(
        ChatFunction.builder()
            .name(f.name())
            .description(f.description())
            .functionalClass(f.funcClass())
            .build()));

    var messages = new ArrayList<ChatMsg>();
    messages.add(new ChatMsgUser(r.prompt()));
    var chatBuilder = ChatRequest.builder()
        .model(Optional.ofNullable(r.model()).orElse(this.model))
        .maxTokens(Optional.ofNullable(r.maxTokens()).orElse(500))
        .n(Optional.ofNullable(r.n()).orElse(1))
        .temperature(Optional.ofNullable(r.temperature()).orElse(0.9))
        .tools(functionExecutor.getToolFunctions());

    // Loop until all functions are executed
    ChatMsgResponse message = null;
    while (true) {
      // Create the request with current messages (the builder is already configured for the rest)
      var chat = chatBuilder.messages(messages).build();
      message = this.openai.chatCompletions()
          .create(chat)
          .join()
          .firstMessage();

      // Check if there is a tool call
      var toolCalls = message.getToolCalls();
      if (toolCalls == null || toolCalls.isEmpty()) {
        return message.getContent();
      }

      // Execute the function call (if it raises an exception send the exception message back)
      var chatToolCall = message.getToolCalls().get(0);
      var result = functionExecutor.execute(chatToolCall.getFunction());
      messages.add(message);
      messages.add(new ChatMsgTool(result.toString(), chatToolCall.getId()));
    }
  }

  @Override
  public List<String> listModels() {
    var models = new ArrayList<String>();
    this.openai.models().getList().whenComplete((r, e) -> {
      if (e != null) {
        System.out.println("Error getting models: " + e.getMessage());
      }
      models.addAll(r.stream().map(ModelResponse::getId).toList());
    }).join();
    return models;
  }
}
