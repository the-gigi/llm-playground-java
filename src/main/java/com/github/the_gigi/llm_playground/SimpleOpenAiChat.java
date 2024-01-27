package com.github.the_gigi.llm_playground;

import static com.github.the_gigi.llm_playground.TextUtil.breakStringIntoLines;


import com.github.the_gigi.llm_playground.Functions.FunctionInfo;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.message.ChatMsg;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgResponse;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgTool;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgUser;
import io.github.sashirestela.openai.domain.chat.tool.ChatFunction;
import io.github.sashirestela.openai.domain.model.ModelResponse;
import io.github.sashirestela.openai.function.FunctionExecutor;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


class SimpleOpenAiClient implements LLMClient {

  private final SimpleOpenAI openai;

  private final String model;
  private final List<FunctionInfo> functions;

  public SimpleOpenAiClient(String baseUrl, String apiKey, String model,
      List<FunctionInfo> functions) {
    this.openai = SimpleOpenAI.builder()
        .urlBase(baseUrl)
        .apiKey(apiKey)
        .build();
    this.model = model;
    this.functions = functions;
  }

  @Override
  public String complete(String prompt, String model) {
    var functionExecutor = new FunctionExecutor();
    this.functions.forEach(f -> functionExecutor.enrollFunction(
        ChatFunction.builder()
            .name(f.name())
            .description(f.description())
            .functionalClass(f.funcClass())
            .build()));

    var messages = new ArrayList<ChatMsg>();
    messages.add(new ChatMsgUser(prompt));
    var chatBuilder = ChatRequest.builder()
        .model(this.model)
        .maxTokens(500)
        .n(1)
        .temperature(0.9)
        .tools(functionExecutor.getToolFunctions());

    // Loop until all functions are executed
    ChatMsgResponse message = null;
    while (true) {
      // Create the request with current messages (the builder is already configured for the rest)
      var r = chatBuilder.messages(messages).build();
      message = this.openai.chatCompletions()
          .create(r)
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
    var modelsResponse = this.openai.models().getList().whenComplete((r, e) -> {
      if (e != null) {
        System.out.println("Error getting models: " + e.getMessage());
      }
      models.addAll(r.stream().map(ModelResponse::getId).toList());
    }).join();
    return models;
  }
}


public class SimpleOpenAiChat  extends BaseChat {

  public SimpleOpenAiChat(
      String baseUrl,
      String apiKey, String defaultModel,
      List<FunctionInfo> functions) {
    super(new SimpleOpenAiClient(baseUrl, apiKey, defaultModel, functions), defaultModel);
  }
}
