package com.github.the_gigi.llm.examples;


import static com.github.the_gigi.llm.examples.functions.FunctionsKt.getOpenAiKotlinTools;

import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.client.OpenAiKotlinClient;
import com.github.the_gigi.llm.examples.shared.Chat;

public class OpenAiJavaChatExample {

  public static void main(String[] args) {
    var tools = getOpenAiKotlinTools();
    var cli = OpenAiKotlinClient.builder(LLMProvider.OPEN_AI).tools(tools).build();
    new Chat(cli, "").start();
  }
}
