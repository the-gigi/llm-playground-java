package com.github.the_gigi.llm.examples;


import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.client.SimpleOpenAiClient;
import com.github.the_gigi.llm.examples.functions.Functions;
import com.github.the_gigi.llm.examples.shared.Chat;

public class SimpleOpenAiCustomChatExample {

  public static void main(String[] args) {
    var tools = Functions.getSimpleOpenAiTools();
    var cli = SimpleOpenAiClient.builder(LLMProvider.CUSTOM)
        .baseUrl(System.getenv("CUSTOM_OPENAI_BASE_URL"))
        .apiKey(System.getenv("CUSTOM_OPENAI_API_KEY"))
        .model(System.getenv("CUSTOM_OPENAI_MODEL"))
        .tools(tools).build();
    new Chat(cli, "").start();
  }
}
