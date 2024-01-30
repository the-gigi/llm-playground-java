package com.github.the_gigi.llm.client;

import static com.github.the_gigi.llm.common.Constants.DEFAULT_ANYSCALE_MODEL;
import static com.github.the_gigi.llm.common.Constants.DEFAULT_OPENAI_MODEL;
import static com.github.the_gigi.llm.examples.functions.Functions.getSimpleOpenAiTools;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.the_gigi.llm.client.LLMClientBuilder.LLMClientLibrary;
import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.domain.CompletionRequest;
import com.github.the_gigi.llm.domain.LLMClient;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimpleOpenAiClientTest {
  LLMClient openAiClient;
  LLMClient anyscaleClient;
  @BeforeEach
  void setUp() {
    var tools = getSimpleOpenAiTools();
    this.openAiClient = new LLMClientBuilder(LLMProvider.OPEN_AI, LLMClientLibrary.SIMPLE_OPENAI)
        .tools(tools)
        .build();
    assertNotNull(this.openAiClient);

    this.anyscaleClient = new LLMClientBuilder(LLMProvider.ANYSCALE, LLMClientLibrary.SIMPLE_OPENAI)
        .tools(tools)
        .build();

    assertNotNull(this.anyscaleClient);
  }
  @Test
  void builder() {
    assertNotNull(SimpleOpenAiClient.builder(LLMProvider.OPEN_AI));
    assertNotNull(SimpleOpenAiClient.builder(LLMProvider.ANYSCALE));
    assertNotNull(SimpleOpenAiClient.builder(LLMProvider.LOCAL));
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
  void completeWithTools() {
    var employees = List.of("John", "Jack", "Jill", "Jane");
    var r = CompletionRequest.builder()
        .prompt("What's the work history of people that work at Uber?")
        .tools(getSimpleOpenAiTools())
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
    var models = this.openAiClient.listModels();
    assertNotNull(models);
    assertFalse(models.isEmpty());
    assertTrue(models.contains(DEFAULT_OPENAI_MODEL));

    models = this.anyscaleClient.listModels();
    assertNotNull(models);
    assertFalse(models.isEmpty());
    assertTrue(models.contains(DEFAULT_ANYSCALE_MODEL));
  }
}