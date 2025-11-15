package academy.aicode.spring_ai.astro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import academy.aicode.spring_ai.distance.DistanceConversionToolService;
import academy.aicode.spring_ai.wiki.WikiToolService;

@RestController
public class AstroBibliaToolController {

  private static final Logger log = LoggerFactory.getLogger(AstroBibliaToolController.class);

  private final ChatClient chatClient;
  private final WikiToolService wikiToolService;
  private final DistanceConversionToolService distanceConversionToolService;

  // Safety guard to avoid accidental huge prompts during demos
  private static final int MAX_PROMPT_LENGTH = 2000;

  public AstroBibliaToolController(ChatClient.Builder chatClientBuilder, WikiToolService wikiToolService,
      DistanceConversionToolService distanceConversionToolService) {
    this.chatClient = chatClientBuilder.build();
    this.wikiToolService = wikiToolService;
    this.distanceConversionToolService = distanceConversionToolService;
    log.info("AstroBibliaToolController initialized (wikiTool={}, distanceTool={})", wikiToolService != null,
        distanceConversionToolService != null);
  }

  /**
   * Implement wiki mode using calls to Wikipedia or other knowledge base
   *
   * @param prompt will be checked at Wikipedia or other knowledge base
   * @return assistant response
   */
  @GetMapping("tool/ama/wiki")
  public String getFromWiki(@RequestParam String prompt) {
    validatePrompt(prompt);
    log.debug("tool/ama/wiki called ({} chars)", prompt.length());
    return chatClient.prompt()
        .system(
            "Responde únicamente con hechos verificables utilizando la información proporcionada. "
                + "Si faltan datos responde 'No pude encontrar información confiable sobre ese tema.'")
        .user(prompt)
        .tools(wikiToolService)
        .call()
        .content();
  }

  @GetMapping("tool/ama/distance")
  public String getDistanceConversion(@RequestParam String prompt) {
    validatePrompt(prompt);
    log.debug("tool/ama/distance called ({} chars)", prompt.length());
    var systemMessage = """
        Eres un asistente útil que convierte distancias astronómicas.
        Usa la herramienta DistanceConverter cuando el usuario pida convertir distancias.
        Las unidades admitidas son: KILÓMETRO, AU, AÑO_LUZ, PARSEC.""";
    return chatClient.prompt()
        .system(systemMessage)
        .user(prompt)
        .tools(distanceConversionToolService)
        .call()
        .content();
  }

  /**
   * Astronomical distance in light time (years, minutes, seconds)
   */
  @GetMapping("tool/ama/distance/light-time")
  public String getDistanceConversionLightTime(@RequestParam String prompt) {
    validatePrompt(prompt);
    log.debug("tool/ama/distance/light-time called ({} chars)", prompt.length());
    var systemMessage = """
        Eres un asistente que proporciona distancias de la tierra a otros cuerpos celestes.
        Usa la herramienta DistanceConverter para devolver la distancia en años luz, minutos luz o segundos luz según sea apropiado.""";
    return chatClient.prompt()
        .system(systemMessage)
        .user(prompt)
        .tools(distanceConversionToolService)
        .call()
        .content();
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

// ToDo: Vector DB (ingest, query)