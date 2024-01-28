package com.github.the_gigi.llm.client;

import java.util.List;

/**
 * Interface for interacting with a language model.
 */
public interface LLMClient {

  /**
   * Generates a completion for the given prompt using the default completion settings.
   *
   * @param prompt The prompt text.
   * @return The generated completion.
   */
  String complete(String prompt);

  /**
   * Generates a completion for the given completion request.
   *
   * @param completionRequest The completion request.
   * @return The generated completion.
   */
  String complete(CompletionRequest completionRequest);

  /**
   * Retrieves a list of available models.
   *
   * @return The list of available models.
   */
  List<String> listModels();
}
