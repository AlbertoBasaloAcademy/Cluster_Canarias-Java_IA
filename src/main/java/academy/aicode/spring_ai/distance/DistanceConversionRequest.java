package academy.aicode.spring_ai.distance;

/**
 * Request payload for distance conversion operations.
 */
public class DistanceConversionRequest {
  private double inputValue;
  private String inputUnit;
  private String outputUnit;

  public double getInputValue() {
    return inputValue;
  }

  public void setInputValue(double inputValue) {
    this.inputValue = inputValue;
  }

  public String getInputUnit() {
    return inputUnit;
  }

  public void setInputUnit(String inputUnit) {
    this.inputUnit = inputUnit;
  }

  public String getOutputUnit() {
    return outputUnit;
  }

  public void setOutputUnit(String outputUnit) {
    this.outputUnit = outputUnit;
  }
}
