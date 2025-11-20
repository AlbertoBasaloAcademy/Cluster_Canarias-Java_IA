package academy.aicode.spring_ai.distance;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing a single endpoint for converting distances between
 * supported units.
 *
 * Validation (fail-fast):
 * - Rejects negative input values.
 * - Rejects unsupported units with a descriptive message listing supported
 * values.
 */
@RestController
@RequestMapping("/api/distance-conversion")
public class DistanceConversionController {
  private static final Logger log = LoggerFactory.getLogger(DistanceConversionController.class);
  private final DistanceConversionService service;

  public DistanceConversionController(DistanceConversionService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<?> convert(@RequestBody DistanceConversionRequest request) {
    log.info("Received conversion request: {} {} to {}", request.getInputValue(), request.getInputUnit(),
        request.getOutputUnit());
    if (request.getInputValue() < 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input value must be non-negative");
    }
    String inputUnit = request.getInputUnit();
    String outputUnit = request.getOutputUnit();
    if (!ConversionFactorProvider.isSupported(inputUnit) || !ConversionFactorProvider.isSupported(outputUnit)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Unsupported unit. Supported: KILOMETER, AU, LIGHT_YEAR, PARSEC");
    }
    var from = ConversionFactorProvider.parseUnit(inputUnit);
    var to = ConversionFactorProvider.parseUnit(outputUnit);
    double converted = service.convert(request.getInputValue(), from, to);
    double factor = service.getConversionFactor(from, to);
    DistanceConversionResponse resp = new DistanceConversionResponse();
    resp.setOriginalValue(request.getInputValue());
    resp.setOriginalUnit(inputUnit.toUpperCase());
    resp.setConvertedValue(converted);
    resp.setConvertedUnit(outputUnit.toUpperCase());
    resp.setConversionFactor(factor);
    resp.setTimestamp(Instant.now());
    return ResponseEntity.ok(resp);
  }
}
