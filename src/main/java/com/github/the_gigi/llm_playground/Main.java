package com.github.the_gigi.llm_playground;

import static com.github.the_gigi.llm_playground.TextUtil.breakStringIntoLines;

import com.github.the_gigi.open_ai_client.OpenAiClient;
import com.github.the_gigi.open_ai_client.OpenAiClientBuilder;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.model.Model;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) {

    // OpenAI
    var openAiClient = createRealOpenAiClient();
    var anyscaleClient = createAnyscaleClient();

    // Simple interaction with OpenAI
    System.out.println("Simple interaction with OpenAI");
    System.out.println("------------------------------");
    simpleInteraction(openAiClient, "gpt-3.5-turbo");

    // Simple interaction with AnyScale
    System.out.println("Simple interaction with AnyScale");
    System.out.println("--------------------------------");
    simpleInteraction(anyscaleClient, "meta-llama/Llama-2-70b-chat-hf");

    // Chat
    System.out.println("Interactive Chat with OpenAI");
    System.out.println("--------------------------------");
    var chat = new Chat(openAiClient);
    chat.start();
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
        .defaultModel("meta-llama/Llama-2-70b-chat-hf")
        .build();
  }
  static private void simpleInteraction(OpenAiClient client, String model) {
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
    var answer = choices.getFirst().getMessage().getContent();
    answer = breakStringIntoLines(answer, 80);

    System.out.println(prompt);
    System.out.println("----- response -----");
    System.out.println(answer);
    System.out.println("------------------");
  }
}