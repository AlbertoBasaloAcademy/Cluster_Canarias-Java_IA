package academy.aicode.spring_ai.vector;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class AstroBibliaVectorService {

  private static final Logger log = LoggerFactory.getLogger(AstroBibliaVectorService.class);

  // Keep MAX_TOKENS visible and documented for workshop attendees.
  private static final int MAX_TOKENS = (int) (8192 * 0.80);

  private static final Pattern WHITESPACE = Pattern.compile("\\s+");

  private final AstroBibliaVectorRepository vectorRepository;

  public VectorStore getVectorStore() {
    return this.vectorRepository.getVectorStore();
  }

  public AstroBibliaVectorService(AstroBibliaVectorRepository vectorRepository) {
    this.vectorRepository = Objects.requireNonNull(vectorRepository, "vectorRepository must not be null");
  }

  /**
   * Validate and add documents to the backing vector store.
   *
   * - Filters out null/empty content
   * - Guards against overly long documents (by token/word count)
   *
   * @param documents list of DTOs containing content + metadata
   * @return list of successfully added Document instances (empty if none)
   */
  public List<Document> addDocuments(List<DocumentRequest> documents) {
    if (documents == null || documents.isEmpty()) {
      log.debug("addDocuments: no documents provided");
      return Collections.emptyList();
    }
    var docs = documents.stream()
        .filter(Objects::nonNull)
        .filter(req -> req.getContent() != null && !req.getContent().trim().isEmpty())
        .filter(req -> {
          int wordCount = WHITESPACE.split(req.getContent().trim()).length;
          if (wordCount > MAX_TOKENS) {
            log.warn("Skipping document (too many tokens): {} tokens (max {})", wordCount, MAX_TOKENS);
            return false;
          }
          return true;
        })
        .map(req -> new Document(req.getContent(), req.getMetadata()))
        .collect(Collectors.toList());

    if (docs.isEmpty()) {
      log.debug("addDocuments: no valid documents after filtering");
      return Collections.emptyList();
    }

    vectorRepository.addDocuments(docs);
    log.info("Added {} documents to vector store", docs.size());
    return docs;
  }

  /**
   * Convenience search that uses sensible defaults.
   *
   * @param prompt search text
   * @return matching documents
   */
  public List<Document> searchDocuments(String prompt) {
    return searchDocuments(prompt, 0.4, 2);
  }

  /**
   * Perform semantic search with explicit similarityThreshold and topK.
   *
   * @param prompt              the search query; if null/blank returns empty list
   * @param similarityThreshold min similarity score [0..1]
   * @param topK                number of top results to return
   * @return list of matching documents
   */
  public List<Document> searchDocuments(String prompt, double similarityThreshold, int topK) {
    if (prompt == null || prompt.trim().isEmpty()) {
      log.debug("searchDocuments called with empty prompt");
      return Collections.emptyList();
    }
    var searchRequest = org.springframework.ai.vectorstore.SearchRequest.builder()
        .query(prompt)
        .similarityThreshold(similarityThreshold)
        .topK(topK)
        .build();
    var results = vectorRepository.semanticSearchByContent(searchRequest);
    log.info("searchDocuments: prompt='{}' -> {} hits", prompt, results == null ? 0 : results.size());
    return results;
  }

}
