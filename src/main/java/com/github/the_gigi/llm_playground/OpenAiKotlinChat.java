package com.github.the_gigi.llm_playground;

import static com.github.the_gigi.llm_playground.OpenAiClientHelperKt.getCompletionResponse;

import com.aallam.openai.api.chat.ChatMessage;
import com.aallam.openai.api.chat.Tool;
import com.aallam.openai.client.OpenAI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OpenAiKotlinChat {
  private final OpenAI client;
  private final Scanner scanner;

  private final String model;

  private final List<FunctionToolCallData> tools;

  public OpenAiKotlinChat(OpenAI client, String model, List<FunctionToolCallData> tools) {
    this.scanner = new Scanner(System.in);
    this.client = client;

    this.model = model;
    this.tools = tools;
  }

  private String getUserInput() {
    return this.scanner.nextLine();
  }

  //  private String chooseModel() {
//    // Get all models
//    var models = getModels(this.client);
//
//    // Print all models with sequential numbers
//    System.out.println("Available models:");
//    System.out.println("-----------------");
//    for (int i = 0; i < models.size(); i++) {
//      var model = models.get(i);
//      System.out.println("[" + (i + 1) + "] " + model.toString());
//    }
//    System.out.println("-----------------");
//    // Ask user to select a model
//    var index = -1;
//    while (index < 0 || index >= models.size()) {
//      System.out.printf("Choose a model by entering its number (1 - %d): ", models.size());
//      var modelNumber = getUserInput();
//      try {
//        index = Integer.parseInt(modelNumber) - 1;
//      } catch (NumberFormatException e) {
//        System.out.println("Invalid model number");
//      }
//    }
//
//    return models.get(index).toString();
//  }

  private String complete(List<String> messages) {

      while (true) {
        var message = getCompletionResponse(this.client, messages, this.model, this.tools);
        if (message.getToolCalls() == null  || message.getToolCalls().isEmpty()) {
          return message.getContent();
        }
      }
  }

  public void start() {
    // Run in a loop until user says "bye"
    var messages = new ArrayList<String>();
    while (true) {
      // Get user input
      System.out.println("---- prompt (type `bye` to exit) ----");
      var prompt = getUserInput();
      if (prompt.equals("bye")) {
        break;
      }

      messages.add("user, " + prompt);

      var response = this.complete(messages);
      System.out.println("----- response -----");
      System.out.println(response);
    }
  }
}
