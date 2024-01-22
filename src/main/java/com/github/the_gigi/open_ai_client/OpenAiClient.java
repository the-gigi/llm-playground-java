package com.github.the_gigi.open_ai_client;


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
import io.reactivex.Flowable;


import java.util.List;

public interface OpenAiClient {

  List<Model> listModels();

  Model getModel(String modelId);

  CompletionResult createCompletion(CompletionRequest request);

  Flowable<CompletionChunk> streamCompletion(CompletionRequest request);

  ChatCompletionResult createChatCompletion(ChatCompletionRequest request);

  Flowable<ChatCompletionChunk> streamChatCompletion(ChatCompletionRequest request);

  EditResult createEdit(EditRequest request);

  EmbeddingResult createEmbeddings(EmbeddingRequest request);

  List<File> listFiles();

  File uploadFile(String purpose, String filepath);

  DeleteResult deleteFile(String fileId);

  File retrieveFile(String fileId);

  FineTuneResult createFineTune(FineTuneRequest request);

  CompletionResult createFineTuneCompletion(CompletionRequest request);

  List<FineTuneResult> listFineTunes();

  FineTuneResult retrieveFineTune(String fineTuneId);

  FineTuneResult cancelFineTune(String fineTuneId);

  List<FineTuneEvent> listFineTuneEvents(String fineTuneId);

  DeleteResult deleteFineTune(String fineTuneId);

  ImageResult createImage(CreateImageRequest request);

  ImageResult createImageEdit(CreateImageEditRequest request, java.io.File image,
      java.io.File mask);

  ImageResult createImageVariation(CreateImageVariationRequest request, java.io.File image);

  TranscriptionResult createTranscription(CreateTranscriptionRequest request, java.io.File audio);

  TranslationResult createTranslation(CreateTranslationRequest request, java.io.File audio);

  ModerationResult createModeration(ModerationRequest request);

  void shutdownExecutor();

}
