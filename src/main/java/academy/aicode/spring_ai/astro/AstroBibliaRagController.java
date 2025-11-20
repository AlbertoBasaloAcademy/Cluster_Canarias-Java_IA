package academy.aicode.spring_ai.astro;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class AstroBibliaRagController {

  private static final Logger log = LoggerFactory.getLogger(AstroBibliaRagController.class);

  private final ChatClient chatClient;

  private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final String WIKIPEDIA_URL = "https://es.wikipedia.org/api/";
  private static final String WIKIPEDIA_SUMMARY_URL = WIKIPEDIA_URL + "rest_v1/page/summary/";
  private static final String USER_AGENT_HEADER = "User-Agent";
  private static final String USER_AGENT_VALUE = "JavaIA/1.0 (+https://aicode.academy)";
  private static final String ACCEPT_HEADER = "Accept";
  private static final String ACCEPT_JSON = "application/json";

  // Safety guard to avoid accidental huge prompts during demos
  private static final int MAX_PROMPT_LENGTH = 2000;

  public AstroBibliaRagController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
    log.info("AstroBibliaRagController initialized");
  }

  /**
   * Ask Me Anything with local data. The prompt is validated before use.
   *
   * @param prompt user question
   * @return assistant response merged with local data
   */
  @GetMapping("rag/ama/local")
  public String getLocalData(@RequestParam String prompt) {
    validatePrompt(prompt);
    log.debug("rag/ama/local called ({} chars)", prompt.length());
    var localInfo = localData(prompt);
    var systemPrompt = "Usa la siguiente información para responder a la pregunta de Astronomía: "
        + localInfo;

    return chatClient.prompt()
        .system(systemPrompt)
        .user(prompt).call().content();
  }

  private String localData(String str) {
    var threeIAtlas = "3I/ATLAS, también conocido como C/2025 N1 y anteriormente como A11pl3Z, es un objeto interestelar​​ descubierto el 1 de julio de 2025 por la estación del Sistema ATLAS de Río Hurtado en Chile, cuando ingresaba en el sistema solar interior a 4,5 UA del Sol y con una velocidad relativa de 61 km/s.";
    if (str.toLowerCase().contains("3i") || str.toLowerCase().contains("atlas")) {
      return threeIAtlas;
    }
    return "No sé sobre ese tema.";
  }

  /**
   * Implement web mode using calls to Wikipedia or other knowledge base.
   *
   * @param prompt user question
   * @return assistant response using verified web info
   */
  @GetMapping("rag/ama/web")
  public String getFromWeb(@RequestParam String prompt) {
    validatePrompt(prompt);
    log.debug("rag/ama/web called ({} chars)", prompt.length());
    var wiki = fetchWikipedia(prompt.strip());
    if (wiki == null || wiki.isBlank()) {
      log.warn("No web info found for topic='{}'", prompt);
      return "No pude encontrar información confiable sobre " + prompt + ".";
    }
    return chatClient.prompt()
        .system(
            "Responde únicamente con hechos verificables utilizando la información proporcionada. "
                + "Si faltan datos responde 'No pude encontrar información confiable sobre ese tema.'")
        .user("Pregunta original: " + prompt + "\nInformación verificada:\n" + wiki)
        .call()
        .content();
  }

  private String fetchWikipedia(String topic) {
    try {
      var encodedTopic = URLEncoder.encode(topic.replace(" ", "_"), StandardCharsets.UTF_8);
      var url = WIKIPEDIA_SUMMARY_URL + encodedTopic;
      var request = HttpRequest.newBuilder(URI.create(url))
          .header(ACCEPT_HEADER, ACCEPT_JSON)
          .header(USER_AGENT_HEADER, USER_AGENT_VALUE)
          .GET()
          .build();
      var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() >= 400) {
        log.error("fetchWikipedia: error {} when fetching {}", response.statusCode(), url);
        return "Obtuve un error al buscar " + topic + " en " + url + ". " + response.statusCode();
      }
      JsonNode root = OBJECT_MAPPER.readTree(response.body());
      var extractNode = root.path("extract");
      return extractNode.isMissingNode() || extractNode.isNull() ? "Respuesta vacía de " + url : extractNode.asText();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.warn("fetchWikipedia interrupted: {}", e.getMessage());
      return e.getMessage();
    } catch (IOException e) {
      log.error("fetchWikipedia IO error: {}", e.getMessage());
      return e.getMessage();
    }
  }

  /**
   * Validate a user-supplied prompt/parameter. Throws a 400 response for
   * null/blank values and a 413 if the content exceeds MAX_PROMPT_LENGTH.
   */
  private void validatePrompt(String prompt) {
    if (prompt == null || prompt.isBlank()) {
      log.debug("validatePrompt: called with empty value");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "parameter must not be empty");
    }
    if (prompt.length() > MAX_PROMPT_LENGTH) {
      log.warn("validatePrompt: parameter length {} exceeds max {}", prompt.length(), MAX_PROMPT_LENGTH);
      throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "parameter too long; reduce size");
    }
  }
}
