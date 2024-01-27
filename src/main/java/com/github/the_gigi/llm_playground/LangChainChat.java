package com.github.the_gigi.llm_playground;


import dev.langchain4j.agent.tool.ToolParameters;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import com.github.the_gigi.llm_playground.Functions.FunctionInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class LangChainClient implements LLMClient {
  ChatLanguageModel client;

  List<FunctionInfo> functions;

  public LangChainClient(String baseUrl, String apiKey, String model, List<FunctionInfo> functions) {
    this.client = OpenAiChatModel.builder()
        .apiKey(apiKey)
        .baseUrl(baseUrl)
        .modelName(model)
        .build();

    this.functions = functions;
  }

  @Override
  public String complete(String prompt, String model) {

    // prepare tools
//    ToolSpecification ts = ToolSpecification.builder()
//        .name("name")
//        .description("description")
//        .parameters(ToolParameters.builder()
//            .type("type")
//            .properties(
//                Collections.singletonMap("foo", Collections.singletonMap("bar", "baz")))
//            .required(Collections.singletonList("foo"))
//            .build())
//        .build();


    return this.client.generate(prompt);

  }

  @Override
  public List<String> listModels() {
    return new ArrayList<>();
  }
}

public class LangChainChat extends BaseChat {

  public LangChainChat(String baseUrl, String apiKey, String model, List<FunctionInfo> functions) {
    super(new LangChainClient(baseUrl, apiKey, model, functions), model);
  }
}
