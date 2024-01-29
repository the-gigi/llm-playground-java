package com.github.the_gigi.llm.playground;

import static com.github.the_gigi.llm.common.Constants.*;
import static com.github.the_gigi.llm.functions.Functions.getSimpleOpenAiTools;
import static com.github.the_gigi.llm.functions.FunctionsKt.getOpenAiKotlinTools;
import static com.github.the_gigi.llm.functions.Functions.getLangChainTools;
import static com.github.the_gigi.llm.functions.Functions.getOpenAiJavaTools;


import com.github.the_gigi.llm.client.LLMClientBuilder.LLMClientLibrary;
import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.client.LangChainClient;
import com.github.the_gigi.llm.client.OpenAiKotlinClient;
import com.github.the_gigi.llm.client.SimpleOpenAiClient;
import com.github.the_gigi.llm.domain.CompletionRequest;
import com.github.the_gigi.llm.functions.LangChainCompanyInfo;
import com.github.the_gigi.openai.client.OpenAiJavaClient;
import com.github.the_gigi.openai.client.OpenAiJavaClientBuilder;
import java.util.List;

import com.github.the_gigi.llm.domain.LLMClient;


public class Main {


  static private OpenAiJavaClient createRealOpenAiClient() {
    var token = System.getenv("OPENAI_API_KEY");
    var base_url = "https://api.openai.com/";
    return new OpenAiJavaClientBuilder(token).baseUrl(base_url).defaultModel("gpt-3.5-turbo")
        .build();
  }

  static private OpenAiJavaClient createAnyscaleClient() {
    var token = System.getenv("ANYSCALE_API_TOKEN");
    var base_url = "https://api.endpoints.anyscale.com/";
    return new OpenAiJavaClientBuilder(token).baseUrl(base_url)
        //.defaultModel("meta-llama/Llama-2-70b-chat-hf")
        .defaultModel("mistralai/Mixtral-8x7B-Instruct-v0.1").build();
  }

  static private OpenAiJavaClient createLocalClient() {
    var token = "dummy";
    return new OpenAiJavaClientBuilder(token).baseUrl(LOCAL_BASE_URL).build();
  }


  static private LLMClient openAiJavaChat(LLMProvider provider, boolean start) {
    var tools = getOpenAiJavaTools();
    var chat = new OpenAiJavaChat(provider, tools.stream().map(f -> (Object) f).toList());
    if (start) {
      chat.start();
    }
    return chat.getClient();
  }


  static private LLMClient openAiKotlinChat(LLMProvider provider, boolean start) {

    var tools = getOpenAiKotlinTools().stream().map(f -> (Object) f).toList();
    var chat = new OpenAiKotlinChat(provider, tools);
    if (start) {
      chat.start();
    }
    return chat.getClient();
  }

  static LLMClient simpleOpenAiChat(LLMProvider provider, Boolean start) {
    var tools = getSimpleOpenAiTools().stream().map(f -> (Object) f).toList();
    var chat = new SimpleOpenAiChat(provider, tools);
    if (start) {
      chat.start();
    }
    return chat.getClient();
  }

  static private LLMClient langChainChat(LLMProvider provider, boolean start) {
    var tools = getLangChainTools();
    var chat = new LangChainChat(provider, tools);
    if (start) {
      chat.start();
    }
    return chat.getClient();
  }


  private static void runOpenAiJava() {
    System.out.println("******** OpenAI Java ********");
      openAiJavaChat(LLMProvider.OPEN_AI, true);
      //openAiJavaChat(LLMProvider.ANYSCALE, true);
      //openAiJavaChat(LLMProvider.LOCAL, false);
  }

  private static void runOpenAiKotlin() {
    System.out.println("******** OpenAI Kotlin ********");
    openAiKotlinChat(LLMProvider.OPEN_AI, true);
//    openAiKotlinChat(LLMProvider.ANYSCALE, true);
//    openAiKotlinChat(LLMProvider.LOCAL, true);

    for (var provider : List.of(LLMProvider.OPEN_AI, LLMProvider.ANYSCALE)) {
      System.out.println("=========== " + provider + " ===========");
      var cli = OpenAiKotlinClient.builder(provider, LLMClientLibrary.OPENAI_KOTLIN)
          .tools(getOpenAiKotlinTools().stream().map(f -> (Object) f).toList())
          .build();

      var response = cli.complete("what's the work history of google employees?");

      System.out.println(response);
    }
  }

  private static void runSimpleOpenAi() {
    System.out.println("******** Simple OpenAI ********");
//    simpleOpenAiChat(LLMProvider.OPEN_AI, true);
//    simpleOpenAiChat(LLMProvider.ANYSCALE, true);
//    simpleOpenAiChat(LLMProvider.LOCAL, true);

    for (var provider : List.of(LLMProvider.OPEN_AI, LLMProvider.ANYSCALE)) {
      System.out.println("=========== " + provider + " ===========");
      var cli = SimpleOpenAiClient.builder(provider)
          .tools(getSimpleOpenAiTools().stream().map(f -> (Object) f).toList())
          .build();

      var response = cli.complete("what's the work history of people that work at uber?");
      System.out.println(response);
    }
  }

  private static void runLangChain() {
    System.out.println("******** LangChain ********");
//    langChainChat(LLMProvider.OPEN_AI, true);
//    langChainChat(LLMProvider.ANYSCALE, true);
//    langChainChat(LLMProvider.LOCAL, true);

    for (var provider : List.of(LLMProvider.OPEN_AI, LLMProvider.ANYSCALE)) {
      System.out.println("=========== " + provider + " ===========");
      var cli = LangChainClient.builder(provider)
          .tools(getLangChainTools())
          .build();

      var response = cli.complete("I'm interested in the work history of people that work at Uber");

      System.out.println(response);
    }
  }

  public static void main(String[] args) {
    //runOpenAiJava();
    //runOpenAiKotlin();
    //runSimpleOpenAi();
    runLangChain();
  }
}
