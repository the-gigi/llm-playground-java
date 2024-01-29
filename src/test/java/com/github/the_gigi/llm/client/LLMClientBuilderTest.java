package com.github.the_gigi.llm.client;

import static com.github.the_gigi.llm.common.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

import com.github.the_gigi.llm.client.LLMClientBuilder.Library;
import com.github.the_gigi.llm.client.LLMClientBuilder.Provider;
import org.junit.jupiter.api.Test;

class LLMClientBuilderTest {


  @Test
  void buildSimpleOpenAiWithOpenAiProvider() {
    var builder = new LLMClientBuilder(Provider.OPEN_AI, Library.SIMPLE_OPENAI);
    var client = builder.build();
    assertNotNull(client);

    var models = client.listModels();
    assertNotNull(models);
    assertFalse(models.isEmpty());
    assertTrue(models.contains(DEFAULT_OPENAI_MODEL));
  }

  @Test
  void buildSimpleOpenAiWithAnyscaleProvider() {
    var builder = new LLMClientBuilder(Provider.ANYSCALE, Library.SIMPLE_OPENAI);
    var client = builder.build();
    assertNotNull(client);

    var models = client.listModels();
    assertNotNull(models);
    assertFalse(models.isEmpty());
    assertTrue(models.contains(DEFAULT_ANYSCALE_MODEL));
  }
}