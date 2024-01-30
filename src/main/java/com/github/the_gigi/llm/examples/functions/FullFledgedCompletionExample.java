package com.github.the_gigi.llm.examples.functions;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.client.SimpleOpenAiClient;
import com.github.the_gigi.llm.client.SimpleOpenAiClient.FunctionInfo;
import com.github.the_gigi.llm.domain.CompletionRequest;
import io.github.sashirestela.openai.function.Functional;
import java.util.List;

public class FullFledgedCompletionExample {

  public static class AnimalNameRequest implements Functional {

    @JsonPropertyDescription("An animal, for example: 'dog' or 'cat'")
    @JsonProperty(required = true)
    public String animal;

    @Override
    public Object execute() {
      return switch (animal) {
        case "dog" -> "Fido";
        case "cat" -> "Garfield";
        case "mouse" -> "Micky";
        case "duck" -> "Donald";
        default -> "I don't have a name for that animal";
      };
    }
  }


  public static void main(String[] args) {
    //var provider = LLMProvider.OPEN_AI;
    var provider = LLMProvider.ANYSCALE;

    var model = provider == LLMProvider.OPEN_AI ? "turbo-gpt-3.5" : "mistralai/Mistral-7B-Instruct-v0.1";
    var tools = List.of(
        (Object) new FunctionInfo(
            "get_animal_name",
            "Return a name for an animal",
            AnimalNameRequest.class));
    var cli = SimpleOpenAiClient.builder(provider)
        .tools(tools)
        .build();
    var prompt = "You are a story teller that writes your stories in lines no longer than "
        + "80 characters. If you need to call functions, call each function only once"
        + "with the same arguments. Now,tell me a story about dog a helping a mouse chased by "
        + "an eagle.";
    var r = CompletionRequest.builder()
        .prompt(prompt)
        .model(model)
        .maxTokens(100)
        .temperature(0.95)
        .topP(0.75)
        .build();
    var response = cli.complete(prompt);

    System.out.println("--------- prompt ---------");
    System.out.println(prompt);
    System.out.println("--------- response ---------");
    System.out.println(response);
    System.out.println("--------------------------");
  }
}
