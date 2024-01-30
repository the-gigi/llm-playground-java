package com.github.the_gigi.llm.examples;


import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.client.SimpleOpenAiClient;
import com.github.the_gigi.llm.domain.CompletionRequest;
import com.github.the_gigi.llm.examples.functions.CompanyInfoRequest;
import com.github.the_gigi.llm.examples.shared.CompletionHelper;

class CustomCompletionWithSimpleOpenAiChainClient {

    public static void main(String[] args) {
        var cli = SimpleOpenAiClient.builder(LLMProvider.OPEN_AI).build();
        var prompt = "Tell me a n upbeat story about black holes";
        var r = CompletionRequest.builder()
            .prompt(prompt)
            .model("gpt-3.5-turbo-1106")
            .maxTokens(100)
            .temperature(0.95)
            .topP(0.75)
            .build();

        CompletionHelper.complete(cli, r);
    }
}
