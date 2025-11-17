package academy.aicode.spring_ai.distance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DistanceConversionController.class)
@Import(DistanceConversionService.class)
class DistanceConversionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldConvertAuToKilometerAndReturnCompleteResponse() throws Exception {
    String requestBody = """
        {
          "inputValue": 1.0,
          "inputUnit": "AU",
          "outputUnit": "KILOMETER"
        }
        """;

    mockMvc.perform(post("/api/distance-conversion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.originalValue").value(1.0))
        .andExpect(jsonPath("$.originalUnit").value("AU"))
        .andExpect(jsonPath("$.convertedValue").value(149_597_870.7))
        .andExpect(jsonPath("$.convertedUnit").value("KILOMETER"))
        .andExpect(jsonPath("$.conversionFactor").value(149_597_870.7))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void shouldConvertLightYearToParsec() throws Exception {
    String requestBody = """
        {
          "inputValue": 3.26156,
          "inputUnit": "LIGHT_YEAR",
          "outputUnit": "PARSEC"
        }
        """;

    mockMvc.perform(post("/api/distance-conversion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.originalValue").value(3.26156))
        .andExpect(jsonPath("$.originalUnit").value("LIGHT_YEAR"))
        .andExpect(jsonPath("$.convertedValue").value(org.hamcrest.Matchers.closeTo(1.0, 0.0001)))
        .andExpect(jsonPath("$.convertedUnit").value("PARSEC"));
  }

  @Test
  void shouldHandleCaseInsensitiveUnits() throws Exception {
    String requestBody = """
        {
          "inputValue": 1.0,
          "inputUnit": "au",
          "outputUnit": "kilometer"
        }
        """;

    mockMvc.perform(post("/api/distance-conversion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.originalUnit").value("AU"))
        .andExpect(jsonPath("$.convertedUnit").value("KILOMETER"));
  }

  @Test
  void shouldReturnBadRequestWhenInputValueIsNegative() throws Exception {
    String requestBody = """
        {
          "inputValue": -5.0,
          "inputUnit": "AU",
          "outputUnit": "KILOMETER"
        }
        """;

    mockMvc.perform(post("/api/distance-conversion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").value("Input value must be non-negative"));
  }

  @Test
  void shouldReturnBadRequestWhenInputUnitIsUnsupported() throws Exception {
    String requestBody = """
        {
          "inputValue": 100.0,
          "inputUnit": "MILE",
          "outputUnit": "KILOMETER"
        }
        """;

    mockMvc.perform(post("/api/distance-conversion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").value("Unsupported unit. Supported: KILOMETER, AU, LIGHT_YEAR, PARSEC"));
  }

  @Test
  void shouldReturnBadRequestWhenOutputUnitIsUnsupported() throws Exception {
    String requestBody = """
        {
          "inputValue": 100.0,
          "inputUnit": "KILOMETER",
          "outputUnit": "METER"
        }
        """;

    mockMvc.perform(post("/api/distance-conversion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").value("Unsupported unit. Supported: KILOMETER, AU, LIGHT_YEAR, PARSEC"));
  }

  @Test
  void shouldReturnBadRequestWhenBothUnitsAreInvalid() throws Exception {
    String requestBody = """
        {
          "inputValue": 100.0,
          "inputUnit": "INVALID1",
          "outputUnit": "INVALID2"
        }
        """;

    mockMvc.perform(post("/api/distance-conversion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldHandleZeroValue() throws Exception {
    String requestBody = """
        {
          "inputValue": 0.0,
          "inputUnit": "AU",
          "outputUnit": "PARSEC"
        }
        """;

    mockMvc.perform(post("/api/distance-conversion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.convertedValue").value(0.0));
  }

  @Test
  void shouldConvertSameUnits() throws Exception {
    String requestBody = """
        {
          "inputValue": 42.0,
          "inputUnit": "PARSEC",
          "outputUnit": "PARSEC"
        }
        """;

    mockMvc.perform(post("/api/distance-conversion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.convertedValue").value(42.0))
        .andExpect(jsonPath("$.conversionFactor").value(1.0));
  }

  @Test
  void shouldConvertLargeValues() throws Exception {
    String requestBody = """
        {
          "inputValue": 1000000.0,
          "inputUnit": "PARSEC",
          "outputUnit": "KILOMETER"
        }
        """;

    mockMvc.perform(post("/api/distance-conversion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.convertedValue").exists());
  }

  @Test
  void shouldIncludeTimestampInResponse() throws Exception {
    String requestBody = """
        {
          "inputValue": 1.0,
          "inputUnit": "AU",
          "outputUnit": "KILOMETER"
        }
        """;

    mockMvc.perform(post("/api/distance-conversion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.timestamp").isNotEmpty());
  }
}
