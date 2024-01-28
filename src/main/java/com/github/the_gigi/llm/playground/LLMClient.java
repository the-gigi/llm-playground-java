package com.github.the_gigi.llm.playground;

import java.util.List;

public interface LLMClient {
  String complete(String prompt, String model);
  List<String> listModels();
}
