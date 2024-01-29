package com.github.the_gigi.llm.client;

import com.github.the_gigi.llm.client.LLMClientBuilder.LLMClientLibrary;
import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.domain.CompletionRequest;
import com.github.the_gigi.llm.domain.LLMClient;
import com.github.the_gigi.openai.client.OpenAiJavaClientBuilder;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest.ChatCompletionRequestFunctionCall;
import com.theokanning.openai.completion.chat.ChatFunction;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.model.Model;
import com.theokanning.openai.service.FunctionExecutor;
import java.util.ArrayList;
import java.util.List;

public class OpenAiJavaClient implements LLMClient {

  private final com.github.the_gigi.openai.client.OpenAiJavaClient client;

  private final List<ChatFunction> functions;

  public static LLMClientBuilder builder(LLMProvider provider) {
    return new LLMClientBuilder(provider, LLMClientLibrary.OPENAI_JAVA);
  }
  public OpenAiJavaClient(
      String baseUrl,
      String apiKey,
      String model,
      List<Object> tools) {
    this.client = new OpenAiJavaClientBuilder(apiKey)
        .baseUrl(baseUrl)
        .defaultModel(model)
        .build();
    this.functions = tools == null ? List.of() : tools.stream().map(t -> (ChatFunction) t).toList();
  }

  @Override
  public String complete(String prompt) {
    return this.complete(CompletionRequest.builder().prompt(prompt).build());
  }

  @Override
  public String complete(CompletionRequest r) {
    var messages = new ArrayList<ChatMessage>();
    messages.add(new ChatMessage(ChatMessageRole.USER.value(), r.prompt()));

    // Create a function executor if there are functions
    FunctionExecutor executor = null;
    if (!this.functions.isEmpty()) {
      executor = new FunctionExecutor(this.functions);
    }

    var builder = ChatCompletionRequest
        .builder()
        .model(r.model())
        .maxTokens(r.maxTokens())
        .n(r.n())
        .temperature(r.temperature())
        .topP(r.topP());

    if (executor != null) {
      builder = builder
          .functions(executor.getFunctions())
          .functionCall(ChatCompletionRequestFunctionCall.of("auto"));
    }

    // Loop until all functions are executed
    ChatMessage message = null;
    while (true) {
      // Create the request with current messages (the builder is already configured for the rest)
      var cr = builder.messages(messages).build();
      message = this.client.createChatCompletion(cr)
          .getChoices()
          .get(0)
          .getMessage();

      // Not a function call just return the result
      var functionCall = message.getFunctionCall();
      if (functionCall == null || executor == null) {
        return message.getContent();
      }

      // Execute the function call (if it raises an exception send the exception message back)
      message = executor.executeAndConvertToMessageHandlingExceptions(functionCall);
      messages.add(message);
    }
  }

  @Override
  public List<String> listModels() {
    return this.client.listModels().stream().map(Model::getId).toList();
  }
}