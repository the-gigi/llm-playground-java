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
    var token = System.getenv("OPENAI_API_KEY");
    var client = new OpenAiClientImpl(token);

//    var models = client.listModels();
//    for (var model : models) {
//      System.out.println(model);
//    }

    var question = "Are you better than Bard? "
        + "What is the best LLM (Large language model) provider?";

    var messages = new ArrayList<ChatMessage>();
    messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), question));

    var r = ChatCompletionRequest
        .builder()
        .model("gpt-3.5-turbo")
        .messages(messages)
        .maxTokens(200)
        .n(1)
        .temperature(0.9)
        .build();

    var result = client.createChatCompletion(r);
    var answer = result.getChoices().getFirst().getMessage().getContent();
    answer = breakStringIntoLines(answer, 80);
    System.out.println(answer);
  }
}