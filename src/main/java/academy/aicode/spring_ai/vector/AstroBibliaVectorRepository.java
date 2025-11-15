package academy.aicode.spring_ai.vector;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Repository;

@Repository
public class AstroBibliaVectorRepository {

  private static final Logger log = LoggerFactory.getLogger(AstroBibliaVectorRepository.class);

  private final VectorStore vectorStore;

  public VectorStore getVectorStore() {
    return this.vectorStore;
  }

  public AstroBibliaVectorRepository(VectorStore vectorStore) {
    this.vectorStore = Objects.requireNonNull(vectorStore, "vectorStore must not be null");
  }

  /**
   * Add documents to the vector store.
   *
   * This method delegates to the configured VectorStore implementation.
   *
   * @param docs non-null list of documents; method returns immediately if
   *             null/empty
   */
  public void addDocuments(List<Document> docs) {
    if (docs == null || docs.isEmpty()) {
      log.debug("addDocuments called with null/empty list; nothing to add");
      return;
    }
    log.debug("Adding {} documents to vector store", docs.size());
    vectorStore.add(docs);
  }

  /**
   * Perform a semantic search in the vector store using the provided
   * {@link SearchRequest}.
   *
   * @param searchRequest the search request; must not be null
   * @return list of matching documents (may be empty but never null)
   */
  public List<Document> semanticSearchByContent(SearchRequest searchRequest) {
    Objects.requireNonNull(searchRequest, "searchRequest must not be null");
    log.debug("Performing semantic search for query='{}', topK={}, similarityThreshold={}",
        searchRequest.getQuery(), searchRequest.getTopK(), searchRequest.getSimilarityThreshold());
    var results = vectorStore.similaritySearch(searchRequest);
    log.debug("Search returned {} documents", results == null ? 0 : results.size());
    return results;
  }
}
