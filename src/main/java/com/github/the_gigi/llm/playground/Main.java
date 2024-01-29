package com.github.the_gigi.llm.playground;

import static com.github.the_gigi.llm.common.Constants.*;
import static com.github.the_gigi.llm.playground.Functions.getSimpleOpenAiTools;
import static com.github.the_gigi.llm.playground.FunctionsKt.getOpenAiKotlinTools;


import com.github.the_gigi.llm.client.LLMClientBuilder.Library;
import com.github.the_gigi.llm.client.LLMClientBuilder.Provider;
import com.github.the_gigi.llm.client.LangChainClient;
import com.github.the_gigi.llm.client.OpenAiKotlinClient;
import com.github.the_gigi.llm.client.SimpleOpenAiClient;
import com.github.the_gigi.openai.client.OpenAiClient;
import com.github.the_gigi.openai.client.OpenAiClientBuilder;
import java.util.List;

import com.github.the_gigi.llm.domain.LLMClient;


public class Main {


  static private OpenAiClient createRealOpenAiClient() {
    var token = System.getenv("OPENAI_API_KEY");
    var base_url = "https://api.openai.com/";
    return new OpenAiClientBuilder(token).baseUrl(base_url).defaultModel("gpt-3.5-turbo").build();
  }

  static private OpenAiClient createAnyscaleClient() {
    var token = System.getenv("ANYSCALE_API_TOKEN");
    var base_url = "https://api.endpoints.anyscale.com/";
    return new OpenAiClientBuilder(token).baseUrl(base_url)
        //.defaultModel("meta-llama/Llama-2-70b-chat-hf")
        .defaultModel("mistralai/Mixtral-8x7B-Instruct-v0.1").build();
  }

  static private OpenAiClient createLocalClient() {
    var token = "dummy";
    return new OpenAiClientBuilder(token).baseUrl(LOCAL_BASE_URL).build();
  }

//  static private LLMClient openAiJavaChat(String provider, boolean start) {
//    OpenAiClient client = switch (provider) {
//      case "openai" -> createRealOpenAiClient();
//      case "anyscale" -> createAnyscaleClient();
//      case "local" -> createLocalClient();
//      default -> throw new IllegalArgumentException("Unknown provider: " + provider);
//    };
//
//    var function = ChatFunction.builder().name("get_work_history")
//        .description("Get work history of all employees of a company")
//        .executor(CompanyInfoRequest.class, Functions::getCompanyInfo).build();
//
//    var chat = new OpenAiJavaChat(client, List.of(function));
//    if (start) {
//      chat.start();
//    }
//    return chat.getClient();
//  }

  static private LLMClient openAiKotlinChat(Provider provider, boolean start) {

    var tools = getOpenAiKotlinTools().stream().map(f -> (Object) f).toList();
    var chat = new OpenAiKotlinChat(provider, tools);
    if (start) {
      chat.start();
    }
    return chat.getClient();
  }

  static LLMClient simpleOpenAiChat(Provider provider, Boolean start) {
    var tools = getSimpleOpenAiTools().stream().map(f -> (Object) f).toList();
    var chat = new SimpleOpenAiChat(provider, tools);
    if (start) {
      chat.start();
    }
    return chat.getClient();
  }

  static private LLMClient langChainChat(Provider provider, boolean start) {
    var tools = List.of((Object) new LangChainCompanyInfo());
    var chat = new LangChainChat(provider, tools);
    if (start) {
      chat.start();
    }
    return chat.getClient();
  }


  private static void runOpenAiJava() {
    System.out.println("******** OpenAI Java ********");
//    openAiJavaChat("openai");
//    openAiJavaChat("anyscale");
//    openAiJavaChat("local");
  }

  private static void runOpenAiKotlin() {
    System.out.println("******** OpenAI Kotlin ********");
      openAiKotlinChat(Provider.OPEN_AI, true);
//    openAiKotlinChat(Provider.ANYSCALE, true);
//    openAiKotlinChat(Provider.LOCAL, true);

    for (var provider : List.of(Provider.OPEN_AI, Provider.ANYSCALE)) {
      System.out.println("=========== " + provider + " ===========");
      var cli = OpenAiKotlinClient.builder(provider, Library.OPENAI_KOTLIN)
          .tools(getOpenAiKotlinTools().stream().map(f -> (Object) f).toList())
          .build();

      var response = cli.complete("what's the work history of google employees?");

      System.out.println(response);
    }
  }

  private static void runSimpleOpenAi() {
    System.out.println("******** Simple OpenAI ********");
//    simpleOpenAiChat(Provider.OPEN_AI, true);
//    simpleOpenAiChat(Provider.ANYSCALE, true);
//    simpleOpenAiChat(Provider.LOCAL, true);

    for (var provider : List.of(Provider.OPEN_AI, Provider.ANYSCALE)) {
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
//    langChainChat(Provider.OPEN_AI, true);
//    langChainChat(Provider.ANYSCALE, true);
//    langChainChat(Provider.LOCAL, true);

    for (var provider : List.of(Provider.OPEN_AI, Provider.ANYSCALE)) {
      System.out.println("=========== " + provider + " ===========");
      var cli = LangChainClient.builder(provider)
          .tools(List.of(new LangChainCompanyInfo()))
          .build();

      var response = cli.complete("I'm interested in the work history of people that work at Uber");
      System.out.println(response);
    }
  }

  public static void main(String[] args) {
    //runOpenAiJava();
    //runOpenAiKotlin();
    runSimpleOpenAi();
    //runLangChain();
  }
}
