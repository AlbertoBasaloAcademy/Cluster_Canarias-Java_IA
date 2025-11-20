package academy.aicode.spring_ai.astro;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import academy.aicode.spring_ai.vector.AstroBibliaVectorService;
import academy.aicode.spring_ai.vector.DocumentRequest;

@RestController
public class AstroBibliaVectorController {
  private static final Logger log = LoggerFactory.getLogger(AstroBibliaVectorController.class);

  // Safety limits for workshop/demo purposes
  private static final int MAX_INGEST_CHAR_LENGTH = 8192; // fallback guard (chars)
  private static final int MAX_TOP_K = 50;

  private final EmbeddingModel embeddingModel;
  private final AstroBibliaVectorService vectorService;
  private final ChatClient ragChatClient;
  private final ChatClient chatClient;

  public AstroBibliaVectorController(ChatClient.Builder builder, EmbeddingModel embeddingModel,
      AstroBibliaVectorService vectorService) {
    this.embeddingModel = embeddingModel;
    this.vectorService = vectorService;
    this.ragChatClient = builder.defaultAdvisors(new QuestionAnswerAdvisor(vectorService.getVectorStore())).build();
    this.chatClient = builder.build();
  }

  /**
   * Compute an embedding for the provided message.
   * Kept as GET for workshop convenience but validated (non-empty).
   */
  @GetMapping("vector/embedding")
  public Map<String, Object> embed(@RequestParam() String message) {
    if (message == null || message.isBlank()) {
      log.debug("embed called with empty message");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message must not be empty");
    }
    log.debug("Computing embedding for message ({} chars)", message.length());
    EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
    return Map.of("embedding", embeddingResponse);
  }

  /**
   * Ingest a single text message into the vector store.
   * Still a GET for convenience, but guarded to avoid accidental huge payloads.
   */
  @GetMapping("vector/ingest")
  public Map<String, Object> ingest(@RequestParam() String message) {
    if (message == null || message.isBlank()) {
      log.debug("ingest called with empty message");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message must not be empty");
    }
    if (message.length() > MAX_INGEST_CHAR_LENGTH) {
      log.warn("Rejecting ingest: message length {} exceeds max {}", message.length(), MAX_INGEST_CHAR_LENGTH);
      throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
          "message too long; reduce size before ingesting");
    }

    var addDocuments = this.vectorService
        .addDocuments(List.of(
            new DocumentRequest(
                message,
                Map.of("source", "AstroBiblia", "length", message.length()))));
    log.info("Ingested document ({} chars) -> added {} documents", message.length(), addDocuments.size());
    return Map.of("added", addDocuments.size());
  }

  /**
   * Perform a semantic search against the vector store.
   * Parameters are clamped to safe ranges to avoid abuse during demos.
   */
  @GetMapping("vector/ama")
  public List<Document> getFromVector(
      @RequestParam String prompt,
      @RequestParam(defaultValue = "0.4") double similarityThreshold,
      @RequestParam(defaultValue = "2") int topK) {
    if (prompt == null || prompt.isBlank()) {
      log.debug("getFromVector called with empty prompt");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "prompt must not be empty");
    }

    // clamp values to safe ranges
    double clampedSim = Math.max(0.0, Math.min(1.0, similarityThreshold));
    int clampedTopK = Math.max(1, Math.min(MAX_TOP_K, topK));

    log.info("Prompt received for vector search: '{}' (similarity={}, topK={})", prompt, clampedSim, clampedTopK);
    var results = vectorService.searchDocuments(prompt, clampedSim, clampedTopK);
    if (results == null || results.isEmpty()) {
      log.info("No vector search results for prompt='{}'", prompt);
      return List.of(new Document("No results found for " + prompt, Map.of()));
    }
    log.info("Vector search for prompt='{}' returned {} results", prompt, results.size());
    return results;
  }

  /**
   * Perform semantic search against the AstroBiblia vector store, and use the
   * chat client to complete the answers.
   */
  @GetMapping("vector/chat")
  public String chatWithVector(@RequestParam String question) {
    if (question == null || question.isBlank()) {
      log.debug("chatWithVector called with empty question");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "question must not be empty");
    }

    log.info("Question received for chat with vector: '{}'", question);
    var response = ragChatClient.prompt()
        .system("Expand this text to a 100 words paragraph")
        .user(question)
        .call().content();
    log.info("Chat with vector for question='{}' completed", question);
    return response;
  }

  /**
   * Perform semantic search against the AstroBiblia vector store, and use the
   * chat client to complete the answers.
   */
  @GetMapping("vector/alone")
  public String chatAlone(@RequestParam String question) {
    if (question == null || question.isBlank()) {
      log.debug("chatAlone called with empty question");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "question must not be empty");
    }

    log.info("Question received for chat alone: '{}'", question);
    var response = chatClient.prompt()
        .system("Expand this text to a 100 words paragraph")
        .user(question)
        .call().content();
    log.info("Chat alone for question='{}' completed", response.length());
    return response;
  }
}