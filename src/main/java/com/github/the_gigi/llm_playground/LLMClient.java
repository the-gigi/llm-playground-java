package com.github.the_gigi.llm_playground;

import java.util.List;

public interface LLMClient {
  String complete(String prompt, String model);
  List<String> listModels();
}
