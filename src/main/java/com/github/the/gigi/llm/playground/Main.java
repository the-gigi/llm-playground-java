package com.github.the.gigi.llm.playground;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import java.util.ArrayList;

public class Main {

  public static String breakStringIntoLines(String input, int maxLineLength) {
    StringBuilder result = new StringBuilder();
    String[] paragraphs = input.split("\n");

    for (String paragraph : paragraphs) {
      String[] words = paragraph.split(" ");
      var line = new StringBuilder();

      for (String word : words) {
        // Check if adding the next word exceeds the max line length
        if (line.length() + word.length() + 1 > maxLineLength) {
          // Add the current line to the result and start a new line
          result.append(line).append("\n");
          line = new StringBuilder();
        }

        if (!line.isEmpty()) {
          line.append(" ");
        }
        line.append(word);
      }

      // Add the last line of the paragraph
      if (!line.isEmpty()) {
        result.append(line);
      }

      // Add a newline character after each paragraph except the last one
      if (!paragraph.equals(paragraphs[paragraphs.length - 1])) {
        result.append("\n");
      }
    }

    return result.toString();
  }


  public static void main(String[] args) {

    // OpenAI

    //var token = System.getenv("OPENAI_API_KEY");
    //var base_url = "https://api.openai.com/";
    //var model = "gpt-3.5-turbo"

    // AnyScale
    var token = System.getenv("ANYSCALE_API_TOKEN");
    var base_url = "https://api.endpoints.anyscale.com/v1/";
    var model = "meta-llama/Llama-2-70b-chat-hf";

    var client = new OpenAiClientImpl(base_url, token);

//    // Works with real OpenAI, not with
//    var models = client.listModels();
//    for (var model : models) {
//      System.out.println(model);
//    }

    var prompt = "Are you better than Bard? "
        + "What is the best LLM (Large language model) provider?";

    var messages = new ArrayList<ChatMessage>();
    messages.add(new ChatMessage(ChatMessageRole.USER.value(), prompt));

    var r = ChatCompletionRequest
        .builder()
        .model(model)
        .messages(messages)
        .maxTokens(500)
        .n(1)
        .temperature(0.9)
        .build();

    var result = client.createChatCompletion(r);
    var answer = result.getChoices().getFirst().getMessage().getContent();
    answer = breakStringIntoLines(answer, 80);

    System.out.println("---- prompt ----");
    System.out.println(prompt);
    System.out.println("----- response -----");
    System.out.println(answer);
    System.out.println("------------------");
  }
}