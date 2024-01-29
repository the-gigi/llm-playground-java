package com.github.the_gigi.llm.playground;


import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.client.LangChainClient;
import java.util.List;


public class LangChainChat extends BaseChat {

  public LangChainChat(LLMProvider provider, List<Object> tools) {
    super(LangChainClient.builder(provider)
        .tools(tools)
        .build(), "");
  }
}
