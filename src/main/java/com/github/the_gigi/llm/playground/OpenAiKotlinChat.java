//package com.github.the_gigi.llm.playground;
//
//import static com.github.the_gigi.llm.playground.OpenAiClientHelperKt.getCompletionResponse;
//import static com.github.the_gigi.llm.playground.OpenAiClientHelperKt.getModels;
//
//import com.aallam.openai.client.OpenAI;
//
//import java.util.List;
//
//
//class OpenAiKotlinClient implements LLMClient {
//
//  private final OpenAI client;
//
//  private final String model;
//
//  private final List<FunctionToolCallData> tools;
//
//  public OpenAiKotlinClient(OpenAI client, String model, List<FunctionToolCallData> tools) {
//    this.client = client;
//    this.model = model;
//    this.tools = tools;
//  }
//
//  @Override
//  public String complete(String prompt, String model) {
//    if (model.isEmpty()) {
//      model = this.model;
//    }
//    var result = getCompletionResponse(this.client, List.of("user," + prompt), model, this.tools);
//
//    // Return the first result
//    return result.getContent();  }
//
//  @Override
//  public List<String> listModels() {
//    try {
//      return getModels(this.client);
//    } catch (Exception e) {
//      return List.of();
//    }
//  }
//}
//
//public class OpenAiKotlinChat extends BaseChat {
//  public OpenAiKotlinChat(OpenAI client, String model, List<FunctionToolCallData> tools) {
//    super(new OpenAiKotlinClient(client, model, tools), "");
//  }
//}
