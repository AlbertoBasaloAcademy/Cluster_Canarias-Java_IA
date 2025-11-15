package academy.aicode.spring_ai.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import academy.aicode.spring_ai.distance.ConversionFactorProvider.Unit;

class ConversionFactorProviderTest {

  @Test
  void shouldReturnKilometerFactorAsOne() {
    double factor = ConversionFactorProvider.getToKilometerFactor(Unit.KILOMETER);

    assertEquals(1.0, factor);
  }

  @Test
  void shouldReturnAuFactorAsIauConstant() {
    double factor = ConversionFactorProvider.getToKilometerFactor(Unit.AU);

    assertEquals(149_597_870.7, factor);
  }

  @Test
  void shouldReturnLightYearFactorAsIauConstant() {
    double factor = ConversionFactorProvider.getToKilometerFactor(Unit.LIGHT_YEAR);

    assertEquals(9_460_730_472_580.8, factor);
  }

  @Test
  void shouldReturnParsecFactorAsIauConstant() {
    double factor = ConversionFactorProvider.getToKilometerFactor(Unit.PARSEC);

    assertEquals(30_856_775_814_913.672, factor);
  }

  @ParameterizedTest
  @ValueSource(strings = { "KILOMETER", "kilometer", "KiLoMeTeR", "AU", "au", "LIGHT_YEAR", "light_year",
      "PARSEC", "parsec" })
  void shouldSupportValidUnits(String unit) {
    boolean supported = ConversionFactorProvider.isSupported(unit);

    assertTrue(supported);
  }

  @ParameterizedTest
  @ValueSource(strings = { "MILE", "METER", "INCH", "INVALID", "" })
  void shouldNotSupportInvalidUnits(String unit) {
    boolean supported = ConversionFactorProvider.isSupported(unit);

    assertFalse(supported);
  }

  @Test
  void shouldParseKilometerUnit() {
    Unit unit = ConversionFactorProvider.parseUnit("kilometer");

    assertEquals(Unit.KILOMETER, unit);
  }

  @Test
  void shouldParseAuUnitCaseInsensitive() {
    Unit unit = ConversionFactorProvider.parseUnit("Au");

    assertEquals(Unit.AU, unit);
  }

  @Test
  void shouldThrowExceptionWhenParsingInvalidUnit() {
    assertThrows(IllegalArgumentException.class, () -> {
      ConversionFactorProvider.parseUnit("INVALID");
    });
  }

  @Test
  void shouldReturnAllFactors() {
    Map<Unit, Double> factors = ConversionFactorProvider.getAllFactors();

    assertEquals(4, factors.size());
    assertEquals(1.0, factors.get(Unit.KILOMETER));
    assertEquals(149_597_870.7, factors.get(Unit.AU));
    assertEquals(9_460_730_472_580.8, factors.get(Unit.LIGHT_YEAR));
    assertEquals(30_856_775_814_913.672, factors.get(Unit.PARSEC));
  }

  @Test
  void shouldReturnImmutableFactorsMap() {
    Map<Unit, Double> factors = ConversionFactorProvider.getAllFactors();

    assertThrows(UnsupportedOperationException.class, () -> {
      factors.put(Unit.KILOMETER, 2.0);
    });
  }
}
