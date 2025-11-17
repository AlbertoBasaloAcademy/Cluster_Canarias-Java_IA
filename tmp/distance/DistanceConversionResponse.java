package academy.aicode.spring_ai.distance;

import java.time.Instant;

/**
 * Response payload conveying original and converted values alongside metadata.
 */
public class DistanceConversionResponse {
  private double originalValue;
  private String originalUnit;
  private double convertedValue;
  private String convertedUnit;
  private double conversionFactor;
  private Instant timestamp;

  public double getOriginalValue() {
    return originalValue;
  }

  public void setOriginalValue(double originalValue) {
    this.originalValue = originalValue;
  }

  public String getOriginalUnit() {
    return originalUnit;
  }

  public void setOriginalUnit(String originalUnit) {
    this.originalUnit = originalUnit;
  }

  public double getConvertedValue() {
    return convertedValue;
  }

  public void setConvertedValue(double convertedValue) {
    this.convertedValue = convertedValue;
  }

  public String getConvertedUnit() {
    return convertedUnit;
  }

  public void setConvertedUnit(String convertedUnit) {
    this.convertedUnit = convertedUnit;
  }

  public double getConversionFactor() {
    return conversionFactor;
  }

  public void setConversionFactor(double conversionFactor) {
    this.conversionFactor = conversionFactor;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }
}
