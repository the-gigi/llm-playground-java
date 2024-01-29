package com.github.the_gigi.llm.playground;

import com.github.the_gigi.llm.client.LLMClientBuilder.LLMClientLibrary;
import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.client.OpenAiKotlinClient;
import java.util.List;


public class OpenAiKotlinChat extends BaseChat {

  public OpenAiKotlinChat(LLMProvider provider, List<Object> tools) {
    super(OpenAiKotlinClient.builder(provider, LLMClientLibrary.OPENAI_KOTLIN)
        .tools(tools)
        .build(), "");
  }
}
