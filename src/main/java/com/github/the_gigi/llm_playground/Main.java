package com.github.the_gigi.llm_playground;

import static com.github.the_gigi.llm_playground.Functions.getFunctionsData;
import static com.github.the_gigi.llm_playground.FunctionsKt.getToolsData;
import static com.github.the_gigi.llm_playground.OpenAiClientHelperKt.createOpenAiKotlinClient;


import com.aallam.openai.client.OpenAI;
import com.github.the_gigi.open_ai_client.OpenAiClient;
import com.github.the_gigi.open_ai_client.OpenAiClientBuilder;
import com.theokanning.openai.completion.chat.ChatFunction;
import java.util.List;

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


  static private void openAiJavaChat(String provider) {
    OpenAiClient client = switch (provider) {
      case "openai" -> createRealOpenAiClient();
      case "anyscale" -> createAnyscaleClient();
      case "local" -> createLocalClient();
      default -> throw new IllegalArgumentException("Unknown provider: " + provider);
    };

    var function = ChatFunction.builder().name("get_work_history")
        .description("Get work history of all employees of a company")
        .executor(CompanyInfoRequest.class, Functions::getCompanyInfo).build();

    var chat = new OpenAiJavaChat(client, List.of(function));
    chat.start();
  }

  static private void openAiKotlinChat(String provider) {
    var toolsData = getToolsData();
    var model = "";
    OpenAI client = null;
    switch (provider) {
      case "openai":
        client = createOpenAiKotlinClient(OPEN_AI_BASE_URL + "v1/", System.getenv("OPENAI_API_KEY"));
        model = DEFAULT_OPENAI_MODEL;
        break;
      case "anyscale":
        client = createOpenAiKotlinClient(ANYSCALE_BASE_URL + "/v1/", System.getenv("ANYSCALE_API_TOKEN"));
        model = DEFAULT_ANYSCALE_MODEL;
        break;
      case "local":
        client = createOpenAiKotlinClient(LOCAL_BASE_URL, "dummy");
        break;
      default:
        throw new IllegalArgumentException("Unknown provider: " + provider);
    }

    //toolsData = List.of(); // No tools! broken in the kotlin client
    var chat = new OpenAiKotlinChat(client, model, toolsData);
    chat.start();
  }

  static private void simpleOpenAiChat(String provider) {
    var baseUrl = "";
    var apiKey = "";
    var model = "";
    var functionsData = getFunctionsData();
    OpenAI client = null;
    switch (provider) {
      case "openai":
        baseUrl = OPEN_AI_BASE_URL;
        apiKey = System.getenv("OPENAI_API_KEY");
        model = DEFAULT_OPENAI_MODEL;
        break;
      case "anyscale":
        baseUrl = ANYSCALE_BASE_URL;
        apiKey = System.getenv("ANYSCALE_API_TOKEN");
        model = DEFAULT_ANYSCALE_MODEL;
        break;
      case "local":
        baseUrl = LOCAL_BASE_URL;
        break;
      default:
        throw new IllegalArgumentException("Unknown provider: " + provider);
    }

    var chat = new SimpleOpenAiChat(baseUrl, apiKey, model, functionsData);
    chat.start();
  }

  static private void langChainChat(String provider) {
    var baseUrl = "";
    var apiKey = "";
    var model = "";
    var functionsData = getFunctionsData();
    switch (provider) {
      case "openai":
        baseUrl = OPEN_AI_BASE_URL + "/v1/";
        apiKey = System.getenv("OPENAI_API_KEY");
        model = DEFAULT_OPENAI_MODEL;
        break;
      case "anyscale":
        baseUrl = ANYSCALE_BASE_URL + "/v1/";
        apiKey = System.getenv("ANYSCALE_API_TOKEN");
        model = DEFAULT_ANYSCALE_MODEL;
        break;
      case "local":
        baseUrl = LOCAL_BASE_URL;
        break;
      default:
        throw new IllegalArgumentException("Unknown provider: " + provider);
    }

    var chat = new LangChainChat(baseUrl, apiKey, model, functionsData);
    chat.start();
  }



  public static void main(String[] args) {
    // --- Use the openai-java library ---
//    openAiJavaChat("openai");
//    openAiJavaChat("anyscale");
//    openAiJavaChat("local");

    // --- Use the openai-kotlin library (Kotlin) ---
    //openAiKotlinChat("openai");
    //openAiKotlinChat("anyscale");
    //openAiKotlinChat("local");

    // --- Use the simple-openai library ---
    //simpleOpenAiChat("openai");
    //simpleOpenAiChat("anyscale");
    //simpleOpenAiChat("local");

    // --- Use the LangChain4j  library ---
    langChainChat("openai");
    //langChainChat("anyscale");
    //langChainChat("local");

  }
}
