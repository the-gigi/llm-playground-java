package com.github.the_gigi.llm.examples;


import com.github.the_gigi.llm.client.LangChainClient;
import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.examples.shared.CompletionHelper;

class BasicCompletionWithLangChainClient {

    public static void main(String[] args) {
        var cli= LangChainClient.builder(LLMProvider.OPEN_AI).build();
        var prompt = "Start with this and return a complete sentence (including the initial expression): `Once upon a time`";
        CompletionHelper.complete(cli, prompt);
    }
}
