//package com.github.the_gigi.llm.playground;
//
//import com.github.the_gigi.openai.client.OpenAiClient;
//import com.theokanning.openai.model.Model;
//import com.theokanning.openai.completion.chat.ChatCompletionRequest;
//import com.theokanning.openai.completion.chat.ChatCompletionRequest.ChatCompletionRequestFunctionCall;
//import com.theokanning.openai.completion.chat.ChatFunction;
//import com.theokanning.openai.completion.chat.ChatMessage;
//import com.theokanning.openai.completion.chat.ChatMessageRole;
//import com.theokanning.openai.service.FunctionExecutor;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//class OpenAiJavaClient implements LLMClient {
//
//  private final OpenAiClient client;
//
//  private final List<ChatFunction> functions;
//
//  public OpenAiJavaClient(OpenAiClient client, List<ChatFunction> functions) {
//    this.client = client;
//    this.functions = functions;
//  }
//
//  @Override
//  public String complete(String prompt, String model) {
//    var messages = new ArrayList<ChatMessage>();
//    messages.add(new ChatMessage(ChatMessageRole.USER.value(), prompt));
//
//    // Create a function executor if there are functions
//    FunctionExecutor executor = null;
//    if (!this.functions.isEmpty()) {
//      executor = new FunctionExecutor(this.functions);
//    }
//
//    var builder = ChatCompletionRequest
//        .builder()
//        .model(model)
//        .maxTokens(100)
//        .n(1)
//        .temperature(0.9);
//
//    if (executor != null) {
//      builder = builder
//          .functions(executor.getFunctions())
//          .functionCall(ChatCompletionRequestFunctionCall.of("auto"));
//    }
//
//    // Loop until all functions are executed
//    ChatMessage message = null;
//    while (true) {
//      // Create the request with current messages (the builder is already configured for the rest)
//      var r = builder.messages(messages).build();
//      message = this.client.createChatCompletion(r)
//          .getChoices()
//          .get(0)
//          .getMessage();
//
//      // Not a function call just return the result
//      var functionCall = message.getFunctionCall();
//      if (functionCall == null || executor == null) {
//        return message.getContent();
//      }
//
//      // Execute the function call (if it raises an exception send the exception message back)
//      message = executor.executeAndConvertToMessageHandlingExceptions(functionCall);
//      messages.add(message);
//    }
//  }
//
//  @Override
//  public List<String> listModels() {
//    return this.client.listModels().stream().map(Model::getId).collect(Collectors.toList());
//  }
//}
//
//public class OpenAiJavaChat extends BaseChat {
//  public OpenAiJavaChat(OpenAiClient client, List<ChatFunction> functions) {
//    super(new OpenAiJavaClient(client, functions), "");
//  }
//}
