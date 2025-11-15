package academy.aicode.spring_ai.vector;

import java.util.Map;

/**
 * Simple Data Transfer Object used when ingesting documents into the vector
 * store.
 *
 * <p>
 * This DTO is intentionally minimal for workshop/demo purposes. It holds the
 * raw text content and an optional arbitrary metadata map that will be stored
 * alongside the document. Metadata entries are free-form and can include
 * things like source, chapter, length, or any domain-specific tags.
 * </p>
 *
 * Usage notes:
 * - The {@code content} should contain the text to be embedded and indexed.
 * - The {@code metadata} map may be null; implementations consuming this DTO
 * must defensively copy or validate entries before persisting.
 */
public class DocumentRequest {
  /**
   * The primary textual content to index/search. Expected to be non-null for
   * valid requests, but callers may construct an empty DTO for testing.
   */
  private String content;

  /**
   * Arbitrary metadata associated with the document. Keys are strings and
   * values are implementation-defined (commonly String, Number or Map).
   */
  private Map<String, Object> metadata;

  /**
   * No-args constructor required by some serialization frameworks (Jackson,
   * etc.). Produces an empty DocumentRequest; callers should set fields
   * explicitly before use.
   */
  public DocumentRequest() {
  }

  /**
   * Convenience constructor for creating a request with content and metadata.
   *
   * @param content  the textual content to store (may be null, but will be
   *                 validated by higher-level services)
   * @param metadata optional metadata map associated with the document
   */
  public DocumentRequest(String content, Map<String, Object> metadata) {
    this.content = content;
    this.metadata = metadata;
  }

  /**
   * Returns the document content.
   *
   * @return content string, may be null for an empty DTO
   */
  public String getContent() {
    return content;
  }

  /**
   * Set the document content. For workshop/demo code we keep setters simple;
   * production code may prefer immutable DTOs or builders.
   *
   * @param content the text content to assign
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Returns the metadata map associated with the document.
   *
   * @return metadata map, may be null
   */
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  /**
   * Assign metadata for the document. Implementations should defensively copy
   * the map if they plan to modify or persist it to avoid shared mutable state.
   *
   * @param metadata metadata map to assign (may be null)
   */
  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }
}
