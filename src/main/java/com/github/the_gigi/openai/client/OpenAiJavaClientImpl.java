package com.github.the_gigi.openai.client;


import com.theokanning.openai.DeleteResult;
import com.theokanning.openai.audio.CreateTranscriptionRequest;
import com.theokanning.openai.audio.CreateTranslationRequest;
import com.theokanning.openai.audio.TranscriptionResult;
import com.theokanning.openai.audio.TranslationResult;
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
import java.util.List;


public class OpenAiJavaClientImpl implements OpenAiJavaClient {

  private final OpenAiService service;
  private final String defaultModel;

  public OpenAiJavaClientImpl(OpenAiService service, String defaultModel) {
    this.service = service;
    this.defaultModel = defaultModel;
  }

  @Override
  public String getDefaultModel() {
    return this.defaultModel;
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
    if (request.getModel() == null) {
      request.setModel(this.defaultModel);
    }
    return this.service.createCompletion(request);
  }

  @Override
  public Flowable<CompletionChunk> streamCompletion(CompletionRequest request) {
    return this.service.streamCompletion(request);
  }

  @Override
  public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
    if (request.getModel() == null) {
      request.setModel(this.defaultModel);
    }
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
