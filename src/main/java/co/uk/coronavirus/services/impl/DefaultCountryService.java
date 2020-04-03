package co.uk.coronavirus.services.impl;

import co.uk.coronavirus.generated.CoronaVirusCountry;
import co.uk.coronavirus.services.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultCountryService implements CountryService
{
   private static final Logger LOG = LoggerFactory.getLogger(DefaultCountryService.class);

   private static final Map<String, String> COUNTRIES_SLUG_MAP = new HashMap<>();

   public static final String POSTMAN_CORONA_VIRUS_BASE_URL = "https://api.covid19api.com";

   public static final String POSTMAN_CORONA_VIRUS_GET_COUNTRY_URL = POSTMAN_CORONA_VIRUS_BASE_URL + "/countries";

   public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

   @Override
   public String getSlugFor(final String countryName)
   {
      return COUNTRIES_SLUG_MAP.get(countryName);
   }

   @PostConstruct
   public void buildAllCountriesMap() throws IOException, InterruptedException
   {
      LOG.info("Building countries map...");
      final HttpClient httpClient = HttpClient.newBuilder().build();

      final HttpRequest httpRequest = HttpRequest.newBuilder()
               .uri(URI.create(POSTMAN_CORONA_VIRUS_GET_COUNTRY_URL))
               .build();

      final String getCountryResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())
               .body();

      final CoronaVirusCountry[] coronaVirusCountriesArray = OBJECT_MAPPER.readValue(getCountryResponse, CoronaVirusCountry[].class);
      Arrays.asList(coronaVirusCountriesArray).forEach(country -> COUNTRIES_SLUG_MAP.put(country.getCountry().toUpperCase(), country.getSlug()));

      LOG.info("Found [{}] countries ", COUNTRIES_SLUG_MAP.size());
   }


}
