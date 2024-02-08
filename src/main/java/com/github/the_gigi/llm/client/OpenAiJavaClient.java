package com.github.the_gigi.llm.client;

import static com.theokanning.openai.service.OpenAiService.defaultClient;
import static com.theokanning.openai.service.OpenAiService.defaultObjectMapper;

import com.github.the_gigi.llm.client.LLMClientBuilder.LLMClientLibrary;
import com.github.the_gigi.llm.client.LLMClientBuilder.LLMProvider;
import com.github.the_gigi.llm.domain.CompletionRequest;
import com.github.the_gigi.llm.domain.LLMClient;
import com.theokanning.openai.DeleteResult;
import com.theokanning.openai.audio.CreateTranscriptionRequest;
import com.theokanning.openai.audio.CreateTranslationRequest;
import com.theokanning.openai.audio.TranscriptionResult;
import com.theokanning.openai.audio.TranslationResult;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.completion.CompletionChunk;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest.ChatCompletionRequestFunctionCall;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatFunction;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.edit.EditRequest;
import com.theokanning.openai.edit.EditResult;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.file.File;
import com.theokanning.openai.finetune.FineTuneEvent;
import com.theokanning.openai.finetune.FineTuneRequest;
import com.theokanning.openai.finetune.FineTuneResult;
import com.theokanning.openai.image.CreateImageEditRequest;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.CreateImageVariationRequest;
import com.theokanning.openai.image.ImageResult;
import com.theokanning.openai.model.Model;
import com.theokanning.openai.moderation.ModerationRequest;
import com.theokanning.openai.moderation.ModerationResult;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import io.reactivex.Flowable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;


class OpenAiJavaClientImpl {

  private final OpenAiService service;
  private final String defaultModel;

  public OpenAiJavaClientImpl(OpenAiService service, String defaultModel) {
    this.service = service;
    this.defaultModel = defaultModel;
  }

  public String getDefaultModel() {
    return this.defaultModel;
  }

  public List<Model> listModels() {
    return this.service.listModels();
  }

  public Model getModel(String modelId) {
    return this.service.getModel(modelId);
  }

  public CompletionResult createCompletion(
      com.theokanning.openai.completion.CompletionRequest request) {
    if (request.getModel() == null) {
      request.setModel(this.defaultModel);
    }
    return this.service.createCompletion(request);
  }

  public Flowable<CompletionChunk> streamCompletion(
      com.theokanning.openai.completion.CompletionRequest request) {
    return this.service.streamCompletion(request);
  }

  public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
    if (request.getModel() == null) {
      request.setModel(this.defaultModel);
    }
    return this.service.createChatCompletion(request);
  }

  public Flowable<ChatCompletionChunk> streamChatCompletion(ChatCompletionRequest request) {
    return this.service.streamChatCompletion(request);
  }

  public EditResult createEdit(EditRequest request) {
    return this.service.createEdit(request);
  }

  public EmbeddingResult createEmbeddings(EmbeddingRequest request) {
    return this.service.createEmbeddings(request);
  }

  public List<File> listFiles() {
    return this.service.listFiles();
  }

  public File uploadFile(String purpose, String filepath) {
    return this.service.uploadFile(purpose, filepath);
  }

  public DeleteResult deleteFile(String fileId) {
    return this.service.deleteFile(fileId);
  }

  public File retrieveFile(String fileId) {
    return this.service.retrieveFile(fileId);
  }

  public FineTuneResult createFineTune(FineTuneRequest request) {
    return this.service.createFineTune(request);
  }

  public CompletionResult createFineTuneCompletion(
      com.theokanning.openai.completion.CompletionRequest request) {
    return this.service.createFineTuneCompletion(request);
  }

  public List<FineTuneResult> listFineTunes() {
    return this.service.listFineTunes();
  }

  public FineTuneResult retrieveFineTune(String fineTuneId) {
    return this.service.retrieveFineTune(fineTuneId);
  }

  public FineTuneResult cancelFineTune(String fineTuneId) {
    return this.service.cancelFineTune(fineTuneId);
  }

  public List<FineTuneEvent> listFineTuneEvents(String fineTuneId) {
    return this.service.listFineTuneEvents(fineTuneId);
  }

  public DeleteResult deleteFineTune(String fineTuneId) {
    return this.service.deleteFineTune(fineTuneId);
  }

  public ImageResult createImage(CreateImageRequest request) {
    return this.service.createImage(request);
  }

  public ImageResult createImageEdit(CreateImageEditRequest request, java.io.File image,
      java.io.File mask) {
    return this.service.createImageEdit(request, image, mask);
  }

  public ImageResult createImageVariation(CreateImageVariationRequest request,
      java.io.File image) {
    return this.service.createImageVariation(request, image);
  }

  public TranscriptionResult createTranscription(CreateTranscriptionRequest request,
      java.io.File audio) {
    return this.service.createTranscription(request, audio);
  }

  public TranslationResult createTranslation(CreateTranslationRequest request,
      java.io.File audio) {
    return this.service.createTranslation(request, audio);
  }

  public ModerationResult createModeration(ModerationRequest request) {
    return this.service.createModeration(request);
  }

  public void shutdownExecutor() {

  }
}


public class OpenAiJavaClient implements LLMClient {

  private final OpenAiJavaClientImpl client;

  private final List<ChatFunction> functions;

  public static LLMClientBuilder builder(LLMProvider provider) {
    return new LLMClientBuilder(provider, LLMClientLibrary.OPENAI_JAVA);
  }

  public OpenAiJavaClient(
      String baseUrl,
      String apiKey,
      String model,
      List<Object> tools) {
    this.client = new OpenAiJavaClientBuilder(apiKey)
        .baseUrl(baseUrl)
        .defaultModel(model)
        .build();
    this.functions = tools == null ? List.of() : tools.stream().map(t -> (ChatFunction) t).toList();
  }

  @Override
  public String complete(String prompt) {
    return this.complete(CompletionRequest.builder().prompt(prompt).build());
  }

  @Override
  public String complete(CompletionRequest r) {
    var messages = new ArrayList<ChatMessage>();
    messages.add(new ChatMessage(ChatMessageRole.USER.value(), r.prompt()));

    // Create a function executor if there are functions
    FunctionExecutor executor = null;
    if (!this.functions.isEmpty()) {
      executor = new FunctionExecutor(this.functions);
    }

    var builder = ChatCompletionRequest
        .builder()
        .model(r.model())
        .maxTokens(r.maxTokens())
        .n(r.n())
        .temperature(r.temperature())
        .topP(r.topP());

    if (executor != null) {
      builder = builder
          .functions(executor.getFunctions())
          .functionCall(ChatCompletionRequestFunctionCall.of("auto"));
    }

    // Loop until all functions are executed
    ChatMessage message = null;
    while (true) {
      // Create the request with current messages (the builder is already configured for the rest)
      var cr = builder.messages(messages).build();
      message = this.client.createChatCompletion(cr)
          .getChoices()
          .get(0)
          .getMessage();

      // Not a function call just return the result
      var functionCall = message.getFunctionCall();
      if (functionCall == null || executor == null) {
        return message.getContent();
      }

      // Execute the function call (if it raises an exception send the exception message back)
      message = executor.executeAndConvertToMessageHandlingExceptions(functionCall);
      messages.add(message);
    }
  }


  public List<String> listModels() {
    return this.client.listModels().stream().map(Model::getId).toList();
  }
}