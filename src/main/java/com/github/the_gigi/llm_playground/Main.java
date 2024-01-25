package com.github.the_gigi.llm_playground;

import static com.github.the_gigi.llm_playground.FunctionsKt.getToolsData;
import static com.github.the_gigi.llm_playground.OpenAiClientHelperKt.createOpenAiKotlinClient;
import static com.github.the_gigi.llm_playground.TextUtil.breakStringIntoLines;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.github.the_gigi.open_ai_client.OpenAiClient;
import com.github.the_gigi.open_ai_client.OpenAiClientBuilder;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatFunction;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;

import java.util.ArrayList;
import java.util.List;
import kotlinx.serialization.Serializable;
import kotlinx.serialization.json.JsonElement;
import org.jetbrains.annotations.NotNull;

public class Main {


  private static final String OPEN_AI_BASE_URL = "https://api.openai.com/v1/";
  private static final String ANYSCALE_BASE_URL = "https://api.endpoints.anyscale.com/v1/";

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

  static private OpenAiClient createDebugClient() {
    var token = "dummy";
    var base_url = "http://localhost:5000";
    return new OpenAiClientBuilder(token)
        .baseUrl(base_url)
        .defaultModel("mistralai/Mixtral-8x7B-Instruct-v0.1")
        .build();
  }

  static private void simpleInteraction(@NotNull OpenAiClient client, String model) {
    var prompt = "Are you better than Bard? "
        + "What is the best LLM (Large language model) provider?";

    var messages = new ArrayList<ChatMessage>();
    messages.add(new ChatMessage(ChatMessageRole.USER.value(), prompt));

    var r = ChatCompletionRequest
        .builder()
        .model(model)
        .messages(messages)
        .maxTokens(100)
        .n(1)
        .temperature(0.9)
        .build();

    var result = client.createChatCompletion(r);
    var choices = result.getChoices();
    var answer = choices.get(0).getMessage().getContent();
    answer = breakStringIntoLines(answer, 80);

    System.out.println(prompt);
    System.out.println("----- response -----");
    System.out.println(answer);
    System.out.println("------------------");
  }


  static private void chat(OpenAiClient client) {
    var function = ChatFunction.builder()
        .name("get_work_history")
        .description("Get work history of all employees of a company")
        .executor(CompanyInfoRequest.class, Main::getCompanyInfo)
        .build();

    var chat = new OpenAiChat(client, List.of(function));
    chat.start();
  }


  public static void main(String[] args) {

    // OpenAI
    var openAiClient = createRealOpenAiClient();
    var anyscaleClient = createAnyscaleClient();
    var debugClient = createDebugClient();

    var openAiKotlinClient = createOpenAiKotlinClient(OPEN_AI_BASE_URL, System.getenv("OPENAI_API_KEY"));
    //var openAiKotlinClient = createOpenAiKotlinClient(ANYSCALE_BASE_URL, System.getenv("ANYSCALE_API_TOKEN"));
    //var openAiKotlinClient = createOpenAiKotlinClient("http://localhost:5000", "dummy"); // debug against local server

//    // Simple interaction with OpenAI
//    System.out.println("Simple interaction with OpenAI");
//    System.out.println("------------------------------");
//    simpleInteraction(openAiClient, "gpt-3.5-turbo");
//
//    // Simple interaction with AnyScale
//    System.out.println("Simple interaction with AnyScale");
//    System.out.println("--------------------------------");
//    simpleInteraction(anyscaleClient, "meta-llama/Llama-2-70b-chat-hf");
//
    // Chat
    System.out.println("Interactive Chat with functions");
    System.out.println("--------------------------------");

    //chat(openAiClient);
    //chat(anyscaleClient);
    //chat(debugClient);

    // Kotlin Chat
    var chat = new OpenAiKotlinChat(openAiKotlinClient, DEFAULT_OPENAI_MODEL, getToolsData());
    chat.start();
  }
}
