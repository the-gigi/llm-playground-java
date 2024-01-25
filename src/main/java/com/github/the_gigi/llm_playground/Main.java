package com.github.the_gigi.llm_playground;

import static com.github.the_gigi.llm_playground.FunctionsKt.getToolsData;
import static com.github.the_gigi.llm_playground.OpenAiClientHelperKt.createOpenAiKotlinClient;


import com.aallam.openai.client.OpenAI;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.github.the_gigi.open_ai_client.OpenAiClient;
import com.github.the_gigi.open_ai_client.OpenAiClientBuilder;
import com.theokanning.openai.completion.chat.ChatFunction;

import java.util.ArrayList;
import java.util.List;
import kotlinx.serialization.Serializable;

public class Main {


  private static final String OPEN_AI_BASE_URL = "https://api.openai.com/v1/";
  private static final String ANYSCALE_BASE_URL = "https://api.endpoints.anyscale.com/v1/";

  private static final String LOCAL_BASE_URL = "http://localhost:5000";

  private static final String DEFAULT_ANYSCALE_MODEL = "mistralai/Mixtral-8x7B-Instruct-v0.1";

  private static final String DEFAULT_OPENAI_MODEL = "gpt-3.5-turbo";

  public static class CompanyInfoRequest {

    @JsonPropertyDescription("Name of company, for example: 'Microsoft' or 'Netflix")
    @JsonProperty(required = true)
    public String name;
  }


  public record EmployeeInfo(String name, List<String> previousCompanies) {

  }

  @Serializable
  public record CompanyInfoResponse(String name, List<EmployeeInfo> employees) {

    ;

  }

  public static CompanyInfoResponse getCompanyInfo(CompanyInfoRequest request) {
    var employees = List.of(
        new EmployeeInfo("John", List.of("SpaceX", "PayPal")),
        new EmployeeInfo("Jack", List.of("Microsoft", "SpaceX", "Netflix")),
        new EmployeeInfo("Jill", List.of("Netflix", "Amazon")),
        new EmployeeInfo("Jane", List.of("Google"))
    );
    return new CompanyInfoResponse(request.name, employees);
  }


  static private OpenAiClient createRealOpenAiClient() {
    var token = System.getenv("OPENAI_API_KEY");
    var base_url = "https://api.openai.com/";
    return new OpenAiClientBuilder(token)
        .baseUrl(base_url)
        .defaultModel("gpt-3.5-turbo")
        .build();
  }

  static private OpenAiClient createAnyscaleClient() {
    var token = System.getenv("ANYSCALE_API_TOKEN");
    var base_url = "https://api.endpoints.anyscale.com/";
    return new OpenAiClientBuilder(token)
        .baseUrl(base_url)
        //.defaultModel("meta-llama/Llama-2-70b-chat-hf")
        .defaultModel("mistralai/Mixtral-8x7B-Instruct-v0.1")
        .build();
  }

  static private OpenAiClient createLocalClient() {
    var token = "dummy";
    return new OpenAiClientBuilder(token)
        .baseUrl(LOCAL_BASE_URL)
        .build();
  }


  static private void javajChat(String provider) {
    OpenAiClient client = null;
    switch (provider) {
      case "openai":
        client = createRealOpenAiClient();
        break;
      case "anyscale":
        client = createAnyscaleClient();
        break;
      case "local":
        client = createLocalClient();
        break;
      default:
        throw new IllegalArgumentException("Unknown provider: " + provider);
    }

    var function = ChatFunction.builder()
        .name("get_work_history")
        .description("Get work history of all employees of a company")
        .executor(CompanyInfoRequest.class, Main::getCompanyInfo)
        .build();

    var chat = new OpenAiChat(client, List.of(function));
    chat.start();
  }

  static private void kotlinChat(String provider) {
    var toolsData = getToolsData();
    var model = "";
    var clients = new ArrayList<OpenAI>();
    OpenAI client = null;
    switch (provider) {
      case "openai":
        client = createOpenAiKotlinClient(OPEN_AI_BASE_URL, System.getenv("OPENAI_API_KEY"));
        model = DEFAULT_OPENAI_MODEL;
        break;
      case "anyscale":
        client = createOpenAiKotlinClient(ANYSCALE_BASE_URL, System.getenv("ANYSCALE_API_TOKEN"));
        model = DEFAULT_ANYSCALE_MODEL;
        break;
      case "local":
        client = createOpenAiKotlinClient(LOCAL_BASE_URL, "dummy");
        break;
      default:
        throw new IllegalArgumentException("Unknown provider: " + provider);
    }
    clients.add(client);
    // add the local client for testing
    clients.add(createOpenAiKotlinClient(LOCAL_BASE_URL, "dummy"));
    var chat = new OpenAiKotlinChat(clients, model, toolsData);
    chat.start();
  }

  public static void main(String[] args) {
    //javaChat("openai");
    //javaChat("anyscale");
    //javaChat("local");

    //kotlinChat("openai");
    kotlinChat("anyscale");
    //kotlinChat("local");
  }
}
