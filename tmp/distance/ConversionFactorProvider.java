package academy.aicode.spring_ai.distance;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides scientific constants and utilities for converting supported units to
 * kilometers.
 */
public class ConversionFactorProvider {
  /** Supported distance units. */
  public enum Unit {
    KILOMETER, AU, LIGHT_YEAR, PARSEC
  }

  private static final Map<Unit, Double> TO_KILOMETER;
  static {
    Map<Unit, Double> map = new HashMap<>();
    map.put(Unit.KILOMETER, 1.0);
    map.put(Unit.AU, 149_597_870.7); // IAU 2012: 1 AU = 149,597,870.7 km
    map.put(Unit.LIGHT_YEAR, 9_460_730_472_580.8); // 1 ly = 9,460,730,472,580.8 km
    map.put(Unit.PARSEC, 30_856_775_814_913.672); // 1 pc = 30,856,775,814,913.672 km
    TO_KILOMETER = Collections.unmodifiableMap(map);
  }

  /**
   * Determines if the given textual unit corresponds to a supported {@link Unit}.
   *
   * @param unit textual unit name (case-insensitive)
   * @return true if supported; false otherwise
   */
  public static boolean isSupported(String unit) {
    try {
      Unit.valueOf(unit.toUpperCase());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  /**
   * Returns the multiplicative factor to convert a {@link Unit} value into
   * kilometers.
   *
   * @param unit the unit to convert from
   * @return factor such that value_in_unit * factor = value_in_kilometers
   * @throws NullPointerException if unit is null
   */
  public static double getToKilometerFactor(Unit unit) {
    // Map contains all enum keys; TO_KILOMETER.get(null) would NPE, keep explicit
    // message
    if (unit == null)
      throw new NullPointerException("unit must not be null");
    return TO_KILOMETER.get(unit);
  }

  /**
   * Parses a textual unit name into a {@link Unit} (case-insensitive).
   *
   * @param unit textual unit name
   * @return parsed {@link Unit}
   * @throws IllegalArgumentException if the unit is not supported
   */
  public static Unit parseUnit(String unit) {
    return Unit.valueOf(unit.toUpperCase());
  }

  /**
   * Returns an unmodifiable view of all unit-to-kilometer factors.
   */
  public static Map<Unit, Double> getAllFactors() {
    return TO_KILOMETER;
  }
}
