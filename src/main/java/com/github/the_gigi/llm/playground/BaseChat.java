package com.github.the_gigi.llm.playground;

import com.github.the_gigi.llm.domain.LLMClient;
import static com.github.the_gigi.llm.playground.TextUtil.breakStringIntoLines;

import java.util.List;
import java.util.Scanner;


public class BaseChat {

  private final LLMClient client;

  private final Scanner scanner;

  protected String model;

  public BaseChat(LLMClient client, String defaultModel) {
    this.client = client;
    this.scanner = new Scanner(System.in);
    this.model = defaultModel;
  }

  public LLMClient getClient() {
    return this.client;
  }

  private String getUserInput() {
    return this.scanner.nextLine();
  }

  protected String chooseModel(List<String> models) {
    // Print all models with sequential numbers
    System.out.println("Available models:");
    System.out.println("-----------------");
    for (int i = 0; i < models.size(); i++) {
      var model = models.get(i);
      System.out.println("[" + (i + 1) + "] " + model);
    }
    System.out.println("-----------------");
    // Ask user to select a model
    var index = -1;
    while (index < 0 || index >= models.size()) {
      System.out.printf("Choose a model by entering its number (1 - %d): ", models.size());
      var modelNumber = getUserInput();
      try {
        index = Integer.parseInt(modelNumber) - 1;
      } catch (NumberFormatException ee) {
        System.out.println("Invalid model number");
      }
    }

    return models.get(index);
  }

  public void start() {
//    if (this.model.isEmpty()) {
//      var models = this.client.listModels();
//      if (!models.isEmpty()) {
//        this.model = chooseModel(models);
//      }
//    }

    // Run in a loop until user says "bye"
    while (true) {
      // Get user input
      System.out.println("---- prompt (type `bye` to exit) ----");
      var prompt = getUserInput();
      if (prompt.equals("bye")) {
        break;
      }

      var response = this.client.complete(prompt);

      System.out.println("----- response -----");
      System.out.println(breakStringIntoLines(response, 80));
    }
  }
}
