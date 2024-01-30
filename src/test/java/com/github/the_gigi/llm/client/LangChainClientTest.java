package com.github.the_gigi.llm.client;

import static com.github.the_gigi.llm.functions.Functions.getLangChainTools;
import static org.junit.jupiter.api.Assertions.*;

import com.github.the_gigi.llm.client.LLMClientBuilder.LLMClientLibrary;
import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.domain.CompletionRequest;
import com.github.the_gigi.llm.domain.LLMClient;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LangChainClientTest {
  LLMClient openAiClient;
  LLMClient anyscaleClient;
  @BeforeEach
  void setUp() {
    var tools = getLangChainTools();
    this.openAiClient = new LLMClientBuilder(LLMProvider.OPEN_AI, LLMClientLibrary.LANG_CHAIN4J)
        .tools(tools)
        .build();
    assertNotNull(this.openAiClient);

    this.anyscaleClient = new LLMClientBuilder(LLMProvider.ANYSCALE, LLMClientLibrary.LANG_CHAIN4J)
        .tools(tools)
        .build();
    assertNotNull(this.anyscaleClient);
  }
  @Test
  void builder() {
    assertNotNull(LangChainClient.builder(LLMProvider.OPEN_AI));
    assertNotNull(LangChainClient.builder(LLMProvider.ANYSCALE));
    assertNotNull(LangChainClient.builder(LLMProvider.LOCAL));
  }

  @Test
  void completeWithJustPrompt() {
    var expected = "Once upon a time";
    var prompt = "complete the sentence: `" + expected + "`. Make sure to include the beginning phrase";
    var result = this.openAiClient.complete(prompt);
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.contains(expected));

    result = this.anyscaleClient.complete(prompt);
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.contains(expected));
  }

  @Test
  void completeWithCompletionRequest() {
    var employees = List.of("John", "Jack", "Jill", "Jane");
    var r = CompletionRequest.builder()
        .prompt("I'm interested in the work history of people that work at Uber")
        .tools(getLangChainTools())
        .build();

    var openAiResult = this.openAiClient.complete(r);
    assertNotNull(openAiResult);
    assertFalse(openAiResult.isEmpty());
    employees.forEach(e -> assertTrue(openAiResult.contains(e)));

    var anyscaleResult = this.anyscaleClient.complete(r);
    assertNotNull(anyscaleResult);
    assertFalse(anyscaleResult.isEmpty());
    employees.forEach(e -> assertTrue(anyscaleResult.contains(e)));
  }

  @Test
  void listModels() {
    // LangChain4J does not support listing models
  }
}
