package com.github.the_gigi.llm.examples;


import static com.github.the_gigi.llm.examples.functions.Functions.getLangChainTools;

import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.client.LangChainClient;
import com.github.the_gigi.llm.examples.shared.CompletionHelper;

class ToolBasedCompletionWithLangChainClient {


  public static void main(String[] args) {
    var tools = getLangChainTools();
    var cli = LangChainClient.builder(LLMProvider.ANYSCALE)
        .tools(tools)
        .build();
    var prompt = "What's the work history of people who work at Google?";
    CompletionHelper.complete(cli, prompt);
  }
}
