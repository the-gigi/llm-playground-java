package com.github.the_gigi.llm.examples;


import static com.github.the_gigi.llm.examples.functions.Functions.getOpenAiJavaTools;

import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.client.OpenAiJavaClient;
import com.github.the_gigi.llm.examples.shared.CompletionHelper;

class ToolBasedCompletionWithOpenAiJavaClient {


    public static void main(String[] args) {
        var tools = getOpenAiJavaTools();
        var cli = OpenAiJavaClient.builder(LLMProvider.OPEN_AI)
            .tools(tools)
            .build();
        var prompt = "What's the work history of people who work at Google?";
        CompletionHelper.complete(cli, prompt);
    }
}
