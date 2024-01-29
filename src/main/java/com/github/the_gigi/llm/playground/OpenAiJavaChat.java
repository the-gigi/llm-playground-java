package com.github.the_gigi.llm.playground;

import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.client.OpenAiJavaClient;
import java.util.List;



public class OpenAiJavaChat extends BaseChat {
  public OpenAiJavaChat(LLMProvider provider, List<Object> tools) {
    super(OpenAiJavaClient.builder(provider).tools(tools).build(), "");
  }
}
