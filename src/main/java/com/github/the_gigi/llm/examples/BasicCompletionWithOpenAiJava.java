package com.github.the_gigi.llm.examples;


import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.client.OpenAiJavaClient;
import com.github.the_gigi.llm.examples.shared.CompletionHelper;

class BasicCompletionWithOpenAiJava {

    public static void main(String[] args) {
        var cli= OpenAiJavaClient.builder(LLMProvider.ANYSCALE).build();
        var prompt = "Start with this and return a complete sentence (including the initial expression): `Once upon a time`";
        CompletionHelper.complete(cli, prompt);
    }
}
