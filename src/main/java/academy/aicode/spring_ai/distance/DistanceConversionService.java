package academy.aicode.spring_ai.distance;

import static java.util.Objects.requireNonNull;

import org.springframework.stereotype.Service;

/**
 * Service that converts distances between supported astronomical units.
 *
 * Inputs are interpreted as exact values in the {@code from} unit and converted
 * to the {@code to} unit using scientifically accurate constants provided by
 * {@link ConversionFactorProvider}.
 */
@Service
public class DistanceConversionService {

  /**
   * Converts a numeric {@code value} from one unit to another.
   *
   * Contract
   * - value: any finite double (negative values are allowed; validation is
   * handled at API boundaries)
   * - from: non-null supported unit
   * - to: non-null supported unit
   *
   * @param value the distance value to convert
   * @param from  the source unit (non-null)
   * @param to    the target unit (non-null)
   * @return the converted value in the target unit
   * @throws NullPointerException if {@code from} or {@code to} is null
   */
  public double convert(double value, ConversionFactorProvider.Unit from, ConversionFactorProvider.Unit to) {
    requireNonNull(from, "from unit must not be null");
    requireNonNull(to, "to unit must not be null");
    double valueInKm = value * ConversionFactorProvider.getToKilometerFactor(from);
    double toKilometerFactor = ConversionFactorProvider.getToKilometerFactor(to);
    return valueInKm / toKilometerFactor;
  }

  /**
   * Returns the multiplicative factor such that
   * {@code value_in_from_unit * factor = value_in_to_unit}.
   *
   * @param from the source unit (non-null)
   * @param to   the target unit (non-null)
   * @return the conversion factor from {@code from} to {@code to}
   * @throws NullPointerException if {@code from} or {@code to} is null
   */
  public double getConversionFactor(ConversionFactorProvider.Unit from, ConversionFactorProvider.Unit to) {
    requireNonNull(from, "from unit must not be null");
    requireNonNull(to, "to unit must not be null");
    double fromFactor = ConversionFactorProvider.getToKilometerFactor(from);
    double toFactor = ConversionFactorProvider.getToKilometerFactor(to);
    return fromFactor / toFactor;
  }
}
