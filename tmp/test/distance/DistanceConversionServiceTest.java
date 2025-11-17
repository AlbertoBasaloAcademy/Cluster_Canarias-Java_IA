package academy.aicode.spring_ai.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import academy.aicode.spring_ai.distance.ConversionFactorProvider.Unit;

class DistanceConversionServiceTest {

  private DistanceConversionService service;

  @BeforeEach
  void setUp() {
    service = new DistanceConversionService();
  }

  @Test
  void shouldConvertKilometerToKilometer() {
    double result = service.convert(100.0, Unit.KILOMETER, Unit.KILOMETER);

    assertEquals(100.0, result, 0.0001);
  }

  @Test
  void shouldConvertAuToKilometer() {
    double result = service.convert(1.0, Unit.AU, Unit.KILOMETER);

    assertEquals(149_597_870.7, result, 0.0001);
  }

  @Test
  void shouldConvertKilometerToAu() {
    double result = service.convert(149_597_870.7, Unit.KILOMETER, Unit.AU);

    assertEquals(1.0, result, 0.0001);
  }

  @Test
  void shouldConvertLightYearToKilometer() {
    double result = service.convert(1.0, Unit.LIGHT_YEAR, Unit.KILOMETER);

    assertEquals(9_460_730_472_580.8, result, 0.0001);
  }

  @Test
  void shouldConvertParsecToKilometer() {
    double result = service.convert(1.0, Unit.PARSEC, Unit.KILOMETER);

    assertEquals(30_856_775_814_913.672, result, 0.0001);
  }

  @Test
  void shouldConvertAuToLightYear() {
    double result = service.convert(63241.077, Unit.AU, Unit.LIGHT_YEAR);

    assertEquals(1.0, result, 0.001);
  }

  @Test
  void shouldConvertParsecToAu() {
    double result = service.convert(1.0, Unit.PARSEC, Unit.AU);

    assertEquals(206264.806247, result, 0.0001);
  }

  @Test
  void shouldConvertLightYearToParsec() {
    double result = service.convert(3.26156, Unit.LIGHT_YEAR, Unit.PARSEC);

    assertEquals(1.0, result, 0.0001);
  }

  @Test
  void shouldHandleZeroValue() {
    double result = service.convert(0.0, Unit.AU, Unit.KILOMETER);

    assertEquals(0.0, result, 0.0001);
  }

  @Test
  void shouldHandleMultipleAuToKilometers() {
    double result = service.convert(5.0, Unit.AU, Unit.KILOMETER);

    assertEquals(747_989_353.5, result, 0.0001);
  }

  @Test
  void shouldCalculateConversionFactorFromAuToKilometer() {
    double factor = service.getConversionFactor(Unit.AU, Unit.KILOMETER);

    assertEquals(149_597_870.7, factor, 0.0001);
  }

  @Test
  void shouldCalculateConversionFactorFromKilometerToAu() {
    double factor = service.getConversionFactor(Unit.KILOMETER, Unit.AU);

    assertEquals(1.0 / 149_597_870.7, factor, 0.00000001);
  }

  @Test
  void shouldCalculateConversionFactorForSameUnit() {
    double factor = service.getConversionFactor(Unit.PARSEC, Unit.PARSEC);

    assertEquals(1.0, factor, 0.0001);
  }

  @Test
  void shouldCalculateConversionFactorFromParsecToLightYear() {
    double factor = service.getConversionFactor(Unit.PARSEC, Unit.LIGHT_YEAR);

    assertEquals(3.26156, factor, 0.0001);
  }

  @Test
  void shouldMaintainPrecisionInChainedConversions() {
    double valueInKm = service.convert(1.0, Unit.AU, Unit.KILOMETER);
    double valueBackToAu = service.convert(valueInKm, Unit.KILOMETER, Unit.AU);

    assertEquals(1.0, valueBackToAu, 0.00001);
  }
}
