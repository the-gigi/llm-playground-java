package com.github.the_gigi.llm_playground;

import static com.github.the_gigi.llm_playground.TextUtil.breakStringIntoLines;

import com.github.the_gigi.open_ai_client.OpenAiClient;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import java.util.ArrayList;
import java.util.Scanner;

public class Chat {
  private final OpenAiClient client;
  private final Scanner scanner;

  private final String model;
  public Chat(OpenAiClient client) {
    this.scanner = new Scanner(System.in);
    this.client = client;
    this.model = chooseModel();
  }


  private String getUserInput() {
    return this.scanner.nextLine();
  }
  private String chooseModel() {
    // Get all models
    var models = this.client.listModels();

    // Print all models with sequential numbers
    System.out.println("Available models:");
    System.out.println("-----------------");
    for (int i = 0; i < models.size(); i++) {
      var model = models.get(i);
      System.out.println("[" + (i + 1) + "] " + model.getId());
      System.out.println("  " + model.getId());
    }
    System.out.println("-----------------");
    // Ask user to select a model
    var index = -1;
    while (index < 0 || index >= models.size()) {
      System.out.printf("Choose a model by entering its number (1 - %d): ", models.size());
      var modelNumber = getUserInput();
      try {
        index = Integer.parseInt(modelNumber) - 1;
      } catch (NumberFormatException e) {
        System.out.println("Invalid model number");
      }
    }

    return models.get(index).getId();
  }

  private String complete(String prompt) {
    var messages = new ArrayList<ChatMessage>();
    messages.add(new ChatMessage(ChatMessageRole.USER.value(), prompt));

    var r = ChatCompletionRequest
        .builder()
        .model(this.model)
        .messages(messages)
        .maxTokens(100)
        .n(1)
        .temperature(0.9)
        .build();

    var result = this.client.createChatCompletion(r);
    var choices = result.getChoices();
    var answer = choices.getFirst().getMessage().getContent();
    return breakStringIntoLines(answer, 80);
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
