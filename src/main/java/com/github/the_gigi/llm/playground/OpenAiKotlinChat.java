package com.github.the_gigi.llm.playground;

import com.aallam.openai.client.OpenAI;

import com.github.the_gigi.llm.client.FunctionToolCallData;
import com.github.the_gigi.llm.client.LLMClientBuilder.Library;
import com.github.the_gigi.llm.client.LLMClientBuilder.Provider;
import com.github.the_gigi.llm.client.LangChainClient;
import com.github.the_gigi.llm.client.OpenAiKotlinClient;
import java.util.List;


public class OpenAiKotlinChat extends BaseChat {

  public OpenAiKotlinChat(Provider provider, List<Object> tools) {
    super(OpenAiKotlinClient.builder(provider, Library.OPENAI_KOTLIN)
        .tools(tools)
        .build(), "");
  }
}
