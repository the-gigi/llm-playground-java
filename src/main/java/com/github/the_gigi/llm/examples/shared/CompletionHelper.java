package com.github.the_gigi.llm.examples.shared;

import static com.github.the_gigi.llm.examples.shared.TextUtil.breakStringIntoLines;

import com.github.the_gigi.llm.domain.CompletionRequest;
import com.github.the_gigi.llm.domain.LLMClient;

public class CompletionHelper {
  public static void complete(LLMClient cli, String prompt) {
    var response = cli.complete(prompt);
    display(prompt, response);
  }

  public static void complete(LLMClient cli, CompletionRequest r) {
    var response = cli.complete(r);
    display(r.prompt(), response);
  }

  private static void display(String prompt, String response) {
    System.out.println("--------- prompt ---------");
    System.out.println(prompt);
    System.out.println("--------- response ---------");
    System.out.println(breakStringIntoLines(response));
    System.out.println("--------------------------");
  }
}
