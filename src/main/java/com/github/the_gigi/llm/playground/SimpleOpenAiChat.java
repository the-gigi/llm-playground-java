package com.github.the_gigi.llm.playground;


import com.github.the_gigi.llm.client.LLMClientBuilder.Library;
import com.github.the_gigi.llm.client.LLMClientBuilder.Provider;
import com.github.the_gigi.llm.client.SimpleOpenAiClient;
import java.util.List;


public class SimpleOpenAiChat extends BaseChat {

  public SimpleOpenAiChat(Provider provider, List<Object> tools) {
    super(SimpleOpenAiClient.builder(provider)
        .tools(tools)
        .build(), "");
  }
}
