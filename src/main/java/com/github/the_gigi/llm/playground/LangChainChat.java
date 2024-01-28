package com.github.the_gigi.llm.playground;


import com.github.the_gigi.llm.client.LLMClientBuilder.Library;
import com.github.the_gigi.llm.client.LLMClientBuilder.Provider;
import com.github.the_gigi.llm.client.LangChainClient;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;


public class LangChainChat extends BaseChat {

  public LangChainChat(Provider provider, List<Object> tools) {
    super(LangChainClient.builder(provider, Library.LANG_CHAIN4J)
        .tools(tools)
        .build(), "");
  }
}
