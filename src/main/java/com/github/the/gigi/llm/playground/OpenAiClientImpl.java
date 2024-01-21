package com.github.the.gigi.llm.playground;

import static com.theokanning.openai.service.OpenAiService.defaultClient;
import static com.theokanning.openai.service.OpenAiService.defaultObjectMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.DeleteResult;
import com.theokanning.openai.audio.CreateTranscriptionRequest;
import com.theokanning.openai.audio.CreateTranslationRequest;
import com.theokanning.openai.audio.TranscriptionResult;
import com.theokanning.openai.audio.TranslationResult;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.completion.CompletionChunk;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
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
import com.theokanning.openai.service.OpenAiService;
import io.reactivex.Flowable;
import java.time.Duration;
import java.util.List;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;


public class OpenAiClientImpl implements OpenAiClient {
  private static final String DEFAULT_BASE_URL = "https://api.openai.com/";
  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
  private static final ObjectMapper mapper = defaultObjectMapper();

  private final OpenAiService service;

  public OpenAiClientImpl(final String token) {
    this(DEFAULT_BASE_URL, token, DEFAULT_TIMEOUT);
  }

  public OpenAiClientImpl(final String base_url, final String token) {
    this(base_url, token, DEFAULT_TIMEOUT);
  }

  public OpenAiClientImpl(final String base_url, final String token, final Duration timeout) {
    ObjectMapper mapper = defaultObjectMapper();
    OkHttpClient client = defaultClient(token, timeout);
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(base_url)
        .client(client)
        .addConverterFactory(JacksonConverterFactory.create(mapper))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();

    var api = retrofit.create(OpenAiApi.class);
    this.service = new OpenAiService(api);
  }

  @Override
  public List<Model> listModels() {
    return this.service.listModels();
  }

  @Override
  public Model getModel(String modelId) {
    return this.service.getModel(modelId);
  }

  @Override
  public CompletionResult createCompletion(CompletionRequest request) {
    return this.service.createCompletion(request);
  }

  @Override
  public Flowable<CompletionChunk> streamCompletion(CompletionRequest request) {
    return this.service.streamCompletion(request);
  }

  @Override
  public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
    return this.service.createChatCompletion(request);
  }

  @Override
  public Flowable<ChatCompletionChunk> streamChatCompletion(ChatCompletionRequest request) {
    return this.service.streamChatCompletion(request);
  }

  @Override
  public EditResult createEdit(EditRequest request) {
    return this.service.createEdit(request);
  }

  @Override
  public EmbeddingResult createEmbeddings(EmbeddingRequest request) {
    return this.service.createEmbeddings(request);
  }

  @Override
  public List<File> listFiles() {
    return this.service.listFiles();
  }

  @Override
  public File uploadFile(String purpose, String filepath) {
    return this.service.uploadFile(purpose, filepath);
  }

  @Override
  public DeleteResult deleteFile(String fileId) {
    return this.service.deleteFile(fileId);
  }

  @Override
  public File retrieveFile(String fileId) {
    return this.service.retrieveFile(fileId);
  }

  @Override
  public FineTuneResult createFineTune(FineTuneRequest request) {
    return this.service.createFineTune(request);
  }

  @Override
  public CompletionResult createFineTuneCompletion(CompletionRequest request) {
    return this.service.createFineTuneCompletion(request);
  }

  @Override
  public List<FineTuneResult> listFineTunes() {
    return this.service.listFineTunes();
  }

  @Override
  public FineTuneResult retrieveFineTune(String fineTuneId) {
    return this.service.retrieveFineTune(fineTuneId);
  }

  @Override
  public FineTuneResult cancelFineTune(String fineTuneId) {
    return this.service.cancelFineTune(fineTuneId);
  }

  @Override
  public List<FineTuneEvent> listFineTuneEvents(String fineTuneId) {
    return this.service.listFineTuneEvents(fineTuneId);
  }

  @Override
  public DeleteResult deleteFineTune(String fineTuneId) {
    return this.service.deleteFineTune(fineTuneId);
  }

  @Override
  public ImageResult createImage(CreateImageRequest request) {
    return this.service.createImage(request);
  }

  @Override
  public ImageResult createImageEdit(CreateImageEditRequest request, java.io.File image,
      java.io.File mask) {
    return this.service.createImageEdit(request, image, mask);
  }

  @Override
  public ImageResult createImageVariation(CreateImageVariationRequest request, java.io.File image) {
    return this.service.createImageVariation(request, image);
  }

  @Override
  public TranscriptionResult createTranscription(CreateTranscriptionRequest request,
      java.io.File audio) {
    return this.service.createTranscription(request, audio);
  }

  @Override
  public TranslationResult createTranslation(CreateTranslationRequest request, java.io.File audio) {
    return this.service.createTranslation(request, audio);
  }

  @Override
  public ModerationResult createModeration(ModerationRequest request) {
    return this.service.createModeration(request);
  }

  @Override
  public void shutdownExecutor() {

  }
}