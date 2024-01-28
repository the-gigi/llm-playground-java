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


//interface Assistant {
//
//  String chat(String userMessage);
//}
//
//class LangChainClient implements LLMClient {
//
//  Assistant assistant;
//
//
//  public LangChainClient(String baseUrl, String apiKey, String model,
//      @NotNull List<Object> tools) {
//    var client = OpenAiChatModel.builder()
//        .apiKey(apiKey)
//        .baseUrl(baseUrl)
//        .modelName(model)
//        .build();
//
//    this.assistant = AiServices.builder(Assistant.class)
//        .chatLanguageModel(client)
//        .tools(tools)
//        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
//        .build();
//  }
//
//  @Override
//  public String complete(String prompt, String model) {
//    return this.assistant.chat(prompt);
//
//
//
//    //return this.client.generate(prompt);
//  }
//
//  @Override
//  public List<String> listModels() {
//    return new ArrayList<>();
//  }
//}

public class LangChainChat extends BaseChat {

  public LangChainChat(Provider provider, Library library, List<Object> tools) {
    super(LangChainClient.builder(provider, library)
        .tools(tools)
        .build(), "");
  }
}
