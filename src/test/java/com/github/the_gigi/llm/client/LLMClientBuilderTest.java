package com.github.the_gigi.llm.client;

import static com.github.the_gigi.llm.common.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

import com.github.the_gigi.llm.client.LLMClientBuilder.LLMClientLibrary;
import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import org.junit.jupiter.api.Test;

class LLMClientBuilderTest {


  @Test
  void buildSimpleOpenAiWithOpenAiProvider() {
    var builder = new LLMClientBuilder(LLMProvider.OPEN_AI, LLMClientLibrary.SIMPLE_OPENAI);
    var client = builder.build();
    assertNotNull(client);

    var models = client.listModels();
    assertNotNull(models);
    assertFalse(models.isEmpty());
    assertTrue(models.contains(DEFAULT_OPENAI_MODEL));
  }

  @Test
  void buildSimpleOpenAiWithAnyscaleProvider() {
    var builder = new LLMClientBuilder(LLMProvider.ANYSCALE, LLMClientLibrary.SIMPLE_OPENAI);
    var client = builder.build();
    assertNotNull(client);

    var models = client.listModels();
    assertNotNull(models);
    assertFalse(models.isEmpty());
    assertTrue(models.contains(DEFAULT_ANYSCALE_MODEL));
  }
}