package com.github.the_gigi.llm.client;

import static org.junit.jupiter.api.Assertions.*;

import com.github.the_gigi.llm.client.LLMClientBuilder.Library;
import com.github.the_gigi.llm.client.LLMClientBuilder.Provider;
import com.github.the_gigi.llm.domain.LLMClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LangChainClientTest {
  LLMClient openAiClient;
  LLMClient anyscaleClient;
  @BeforeEach
  void setUp() {
    this.openAiClient = new LLMClientBuilder(Provider.OPEN_AI, Library.LANG_CHAIN4J).build();
    assertNotNull(this.openAiClient);

    this.anyscaleClient = new LLMClientBuilder(Provider.ANYSCALE, Library.LANG_CHAIN4J).build();

    assertNotNull(this.anyscaleClient);
  }
  @Test
  void builder() {
    assertNotNull(LangChainClient.builder(Provider.OPEN_AI));
    assertNotNull(LangChainClient.builder(Provider.ANYSCALE));
    assertNotNull(LangChainClient.builder(Provider.LOCAL));
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
  }

  @Test
  void listModels() {
  }
}