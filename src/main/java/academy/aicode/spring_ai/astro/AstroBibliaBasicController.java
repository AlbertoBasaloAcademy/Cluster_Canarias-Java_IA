package academy.aicode.spring_ai.astro;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Flux;

/**
 * Basic demo controller showing various ways to call the ChatClient.
 *
 * This controller is intentionally small and uses GET endpoints for workshop
 * convenience. For production APIs prefer POST when sending non-trivial
 * payloads. Methods include simple guards and logging similar to the vector
 * controller used in the workshop.
 */
@RestController
public class AstroBibliaBasicController {
  private static final Logger log = LoggerFactory.getLogger(AstroBibliaBasicController.class);

  private final ChatClient chatClient;

  private static final String ASTRONOMY_SYSTEM_MESSAGE = "Eres un experto en Astronomía. Responde solo preguntas relacionadas con la Astronomía. Si la pregunta no está relacionada con la Astronomía, responde con 'No sé sobre ese tema.'";

  // Safety guard to avoid accidental huge prompts during demos
  private static final int MAX_PROMPT_LENGTH = 2000;

  public AstroBibliaBasicController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  /**
   * Aka "Ask Me Anything" - forwards the user prompt to the chat client.
   * Performs basic validation on the prompt and logs the request for demos.
   *
   * @param prompt user question
   * @return plain text chat response
   */
  @GetMapping("basic/ama")
  public String getAnything(@RequestParam String prompt) {
    validatePrompt(prompt);
    return chatClient.prompt()
        .user(prompt).call().content();
  }

  /**
   * Ask only Astronomy related questions. Non-astronomy topics are handled by
   * the system message. Input validated similarly to other endpoints.
   *
   * @param prompt user question
   * @return plain text chat response constrained to astronomy domain
   */
  @GetMapping("basic/astro")
  public String getAstronomy(@RequestParam String prompt) {
    validatePrompt(prompt);
    return chatClient.prompt()
        .system(ASTRONOMY_SYSTEM_MESSAGE)
        .user(prompt).call().content();
  }

  /**
   * Ask about a planet. Uses simple string replacement to craft the user
   * prompt. Validates the planet parameter and logs the constructed prompt.
   *
   * @param planet planet name
   * @return chat response describing the planet
   */
  @GetMapping("basic/planet")
  public String getPlanetInfo(@RequestParam String planet) {
    validatePrompt(planet);
    var userPromptTemplate = "Proporciona una breve descripción del planeta {{planet}} incluyendo sus características clave y cualquier dato interesante.";
    var userPrompt = userPromptTemplate.replace("{{planet}}", planet);

    return chatClient.prompt()
        .system(ASTRONOMY_SYSTEM_MESSAGE)
        .user(userPrompt).call().content();
  }

  /**
   * Ask about a planet's satellites. Uses lambda-style parameter substitution
   * supported by the ChatClient builder. Validates input and logs the call.
   *
   * @param planet planet name
   * @return chat response listing satellites
   */
  @GetMapping("basic/planet/satellites")
  public String getPlanetSatellites(@RequestParam String planet) {
    validatePrompt(planet);

    return chatClient.prompt()
        .system(ASTRONOMY_SYSTEM_MESSAGE)
        .user(u -> {
          u.text("¿Cuáles son los satélites del planeta {planet} ?");
          u.param("planet", planet);
        }).call().content();
  }

  /**
   * Ask about a planet's satellites and map the response into a structured
   * record type. Useful to demonstrate typed deserialization from the model
   * response.
   *
   * @param planet planet name
   * @return structured Satellites record
   */
  @GetMapping("basic/planet/satellites/structured")
  public Satellites getPlanetSatellitesStructured(@RequestParam String planet) {
    validatePrompt(planet);

    return chatClient.prompt()
        .system(ASTRONOMY_SYSTEM_MESSAGE)
        .user(u -> {
          u.text("¿Cuáles son los satélites del planeta {planet} ?");
          u.param("planet", planet);
        }).call().entity(Satellites.class);
  }

  /**
   * Ask about a planet's satellites and stream the response back to the
   * caller. Returns a Reactor Flux of partial content strings.
   *
   * @param planet planet name
   * @return streaming content as Flux<String>
   */
  @GetMapping("basic/planet/satellites/stream")
  public Flux<String> getPlanetSatellitesStream(@RequestParam String planet) {
    validatePrompt(planet);

    return chatClient.prompt()
        .system(ASTRONOMY_SYSTEM_MESSAGE)
        .user(u -> {
          u.text("¿Cuáles son los satélites del planeta {planet} ?");
          u.param("planet", planet);
        }).stream().content();
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
    var tokens = prompt.length() / 4; // Simplified token count for demo purposes
    log.debug("validatePrompt: prompt tokens {}", tokens);
  }

}

record Satellite(String name, double radius, double mass) {
}

record Satellites(List<Satellite> satellites) {
}