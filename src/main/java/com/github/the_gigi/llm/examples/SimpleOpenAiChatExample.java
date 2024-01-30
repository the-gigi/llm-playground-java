package com.github.the_gigi.llm.examples;


import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.client.LangChainClient;
import com.github.the_gigi.llm.client.SimpleOpenAiClient;
import com.github.the_gigi.llm.examples.functions.Functions;
import com.github.the_gigi.llm.examples.shared.Chat;

public class SimpleOpenAiChatExample {

  public static void main(String[] args) {
    var tools = Functions.getSimpleOpenAiTools();
    var cli = SimpleOpenAiClient.builder(LLMProvider.ANYSCALE).tools(tools).build();
    new Chat(cli, "").start();
  }
}
