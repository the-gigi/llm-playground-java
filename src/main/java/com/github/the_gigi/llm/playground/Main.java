package com.github.the_gigi.llm.playground;

import static com.github.the_gigi.llm.playground.Functions.getFunctionsData;
import static com.github.the_gigi.llm.playground.FunctionsKt.getToolsData;
import static com.github.the_gigi.llm.playground.OpenAiClientHelperKt.createOpenAiKotlinClient;


import com.aallam.openai.client.OpenAI;
import com.github.the_gigi.llm.client.LLMClientBuilder.Library;
import com.github.the_gigi.llm.client.LLMClientBuilder.Provider;
import com.github.the_gigi.llm.client.SimpleOpenAiClient;
import com.github.the_gigi.openai.client.OpenAiClient;
import com.github.the_gigi.openai.client.OpenAiClientBuilder;
import java.util.List;

import com.github.the_gigi.llm.client.LLMClient;



public class Main {


  private static final String OPEN_AI_BASE_URL = "https://api.openai.com/";
  private static final String ANYSCALE_BASE_URL = "https://api.endpoints.anyscale.com";

  private static final String LOCAL_BASE_URL = "http://localhost:5000";

  private static final String DEFAULT_ANYSCALE_MODEL = "mistralai/Mixtral-8x7B-Instruct-v0.1";

  private static final String DEFAULT_OPENAI_MODEL = "gpt-3.5-turbo";


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

//  static private LLMClient openAiKotlinChat(String provider, boolean start) {
//    var toolsData = getToolsData();
//    var model = "";
//    OpenAI client = null;
//    switch (provider) {
//      case "openai":
//        client = createOpenAiKotlinClient(OPEN_AI_BASE_URL + "v1/", System.getenv("OPENAI_API_KEY"));
//        model = DEFAULT_OPENAI_MODEL;
//        break;
//      case "anyscale":
//        client = createOpenAiKotlinClient(ANYSCALE_BASE_URL + "/v1/", System.getenv("ANYSCALE_API_TOKEN"));
//        model = DEFAULT_ANYSCALE_MODEL;
//        break;
//      case "local":
//        client = createOpenAiKotlinClient(LOCAL_BASE_URL, "dummy");
//        break;
//      default:
//        throw new IllegalArgumentException("Unknown provider: " + provider);
//    }
//
//    //toolsData = List.of(); // No tools! broken in the kotlin client
//    var chat = new OpenAiKotlinChat(client, model, toolsData);
//    if (start) {
//      chat.start();
//    }
//    return chat.getClient();
//  }

  static LLMClient simpleOpenAiChat(Provider provider, Boolean start) {
    var tools = getFunctionsData().stream().map(f -> (Object) f).toList();
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


  public static void main(String[] args) {
//    var clients = Map<String, LLMClient>.of(
//        "openAiJava-openai", openAiJavaChat("openai", false),
//        "anyscale", openAiJavaChat("anyscale", false),
//        "local", openAiJavaChat("local", false)
//    );
    // --- Use the openai-java library ---
//    var cli1 = openAiJavaChat("openai");
//    var cli2 = openAiJavaChat("anyscale");
//    var cli3 = openAiJavaChat("local");

    // --- Use the openai-kotlin library (Kotlin) ---
    //var clie4 = openAiKotlinChat("openai");
    //var cli5 =openAiKotlinChat("anyscale");
    //openAiKotlinChat("local");

    // --- Use the simple-openai library ---
    //simpleOpenAiChat(Provider.OPEN_AI, true);
//    simpleOpenAiChat(Provider.ANYSCALE, true);
//    simpleOpenAiChat(Provider.LOCAL, true);

//    for (var provider : List.of(Provider.OPEN_AI, Provider.ANYSCALE)) {
//      var cli = SimpleOpenAiClient.builder(provider, Library.SIMPLE_OPENAI)
//          .tools(getFunctionsData().stream().map(f -> (Object) f).toList())
//          .build();
//
//      var response = cli.complete("what's the work history of google employees?");
//      System.out.println("=========== " + provider + " ===========");
//      System.out.println(response);
//    }
//  }

  // --- Use the LangChain4j  library ---
  //langChainChat(Provider.OPEN_AI, true);
  //langChainChat(Provider.ANYSCALE, true);
  //langChainChat(Provider.LOCAL, true);

//    for (var provider : List.of(Provider.OPEN_AI, Provider.ANYSCALE)) {
//      var cli = LangChainClient.builder(provider, Library.LANG_CHAIN4J)
//          .tools(List.of(new LangChainCompanyInfo()))
//          .build();
//
//      var response = cli.complete("I'm interested in the work history of people that work at Uber");
//      System.out.println("=========== " + provider + " ===========");
//      System.out.println(response);
//    }
  }
}
