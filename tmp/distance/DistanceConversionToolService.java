package academy.aicode.spring_ai.distance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class DistanceConversionToolService {

  private static final Logger log = LoggerFactory.getLogger(DistanceConversionToolService.class);
  private final DistanceConversionService distanceConversionService;

  public DistanceConversionToolService(DistanceConversionService distanceConversionService) {
    this.distanceConversionService = distanceConversionService;
  }

  /**
   * Tool entry point for use by AI agents to perform distance conversions.
   */
  @Tool(name = "DistanceConverter", description = "Converts astronomical distances between different units. Supported units: KILOMETER, AU, LIGHT_YEAR, PARSEC.")
  public String convertDistance(double value, String fromUnit, String toUnit) {
    log.info("Distance conversion requested: {} {} to {}", value, fromUnit, toUnit);
    try {
      var from = ConversionFactorProvider.parseUnit(fromUnit);
      var to = ConversionFactorProvider.parseUnit(toUnit);
      var result = distanceConversionService.convert(value, from, to);
      var factor = distanceConversionService.getConversionFactor(from, to);
      log.info("Conversion successful: {} {} = {} {}", value, fromUnit, result, toUnit);
      return String.format("%.6e %s equals %.6e %s (conversion factor: %.6e)",
          value, fromUnit, result, toUnit, factor);
    } catch (IllegalArgumentException e) {
      log.error("Invalid unit provided: {}", e.getMessage());
      return "Error: Invalid unit. Supported units are: KILOMETER, AU, LIGHT_YEAR, PARSEC";
    } catch (Exception e) {
      log.error("Error during conversion: {}", e.getMessage());
      return "Error during conversion: " + e.getMessage();
    }
  }
}
