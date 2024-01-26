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
import io.github.sashirestela.openai.function.FunctionExecutor;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleOpenAiChat {
  private final SimpleOpenAI openai;

  private final Scanner scanner;
  private final List<FunctionInfo> functions;
  private final String model;

  public SimpleOpenAiChat(String baseUrl, String apiKey, String defaultModel , List<FunctionInfo> functions) {
    this.scanner = new Scanner(System.in);
    this.openai = SimpleOpenAI.builder()
        .urlBase(baseUrl)
        .apiKey(apiKey)
        .build();

    this.model = defaultModel.isEmpty() ? chooseModel() : defaultModel;
    this.functions = functions;
  }

  private String getUserInput() {
    return this.scanner.nextLine();
  }
  private String chooseModel() {
    AtomicInteger selectedIndex = new AtomicInteger(-1);
    // Get all models
    var models = this.openai.models().getList().whenComplete((r, e) -> {
      if (e != null) {
        System.out.println("Error getting models: " + e.getMessage());
      }
      // Print all models with sequential numbers
      System.out.println("Available models:");
      System.out.println("-----------------");
      for (int i = 0; i < r.size(); i++) {
        var model = r.get(i);
        System.out.println("[" + (i + 1) + "] " + model.getId());
      }
      System.out.println("-----------------");
      // Ask user to select a model
      var index = -1;
      while (index < 0 || index >= r.size()) {
        System.out.printf("Choose a model by entering its number (1 - %d): ", r.size());
        var modelNumber = getUserInput();
        try {
          index = Integer.parseInt(modelNumber) - 1;
        } catch (NumberFormatException ee) {
          System.out.println("Invalid model number");
        }
      }
      selectedIndex.set(index);
    }).join();
    return models.get(selectedIndex.get()).getId();
  }

  private String complete(String prompt) {
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
        return breakStringIntoLines(message.getContent(), 80);
      }

      // Execute the function call (if it raises an exception send the exception message back)
      var chatToolCall = message.getToolCalls().get(0);
      var result = functionExecutor.execute(chatToolCall.getFunction());
      messages.add(message);
      messages.add(new ChatMsgTool(result.toString(), chatToolCall.getId()));
    }
  }

  public void start() {
    // Run in a loop until user says "bye"
    while (true) {
      // Get user input
      System.out.println("---- prompt (type `bye` to exit) ----");
      var prompt = getUserInput();
      if (prompt.equals("bye")) {
        break;
      }

      var response = this.complete(prompt);

      System.out.println("----- response -----");
      System.out.println(response);
    }
  }
}
