package com.github.the_gigi.llm.client;

import java.util.List;


public interface LLMClient {
  String complete(String prompt);

  String complete(CompletionRequest completionRequest);
  List<String> listModels();
}

