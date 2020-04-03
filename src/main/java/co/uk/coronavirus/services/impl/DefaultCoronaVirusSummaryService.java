package co.uk.coronavirus.services.impl;

import co.uk.coronavirus.generated.CoronaVirusSummary;
import co.uk.coronavirus.services.CoronaVirusSummaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultCoronaVirusSummaryService implements CoronaVirusSummaryService
{
   private static final Logger LOG = LoggerFactory.getLogger(DefaultCoronaVirusSummaryService.class);

   public static final String POSTMAN_CORONA_VIRUS_BASE_URL = "https://api.covid19api.com";

   public static final String POSTMAN_CORONA_VIRUS_GET_SUMMARY_URL = POSTMAN_CORONA_VIRUS_BASE_URL + "/summary";

   public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-mm-yyyy");

   public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

   private Map<String, CoronaVirusSummary> coronavirusSummaryMap = new HashMap<>();

   @Override
   public CoronaVirusSummary getCoronaVirusSummary() throws IOException, InterruptedException
   {
      final String today = SIMPLE_DATE_FORMAT.format(new Date());
      LOG.debug("Checking coronavirus summary for [{}]" , today);
      final CoronaVirusSummary latestCoronaVirusMap = coronavirusSummaryMap.get(today);

      if (latestCoronaVirusMap != null)
      {
         LOG.debug("Coronavirus summary already fetched, returning from cache");
         return latestCoronaVirusMap;
      }
      else
      {
         LOG.debug("First request for coronavirus summary of the day [{}], so building the cache", today);
         final HttpClient httpClient = HttpClient.newBuilder().build();

         final HttpRequest httpRequest = HttpRequest.newBuilder()
                  .uri(URI.create(POSTMAN_CORONA_VIRUS_GET_SUMMARY_URL))
                  .build();

         final String getCoronaVirusSummaryResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())
                  .body();

         final CoronaVirusSummary coronaVirusSummary = OBJECT_MAPPER.readValue(getCoronaVirusSummaryResponse, CoronaVirusSummary.class);

         coronaVirusSummary.getCountries()
                  .removeIf(country -> country.getCountry() == null || country.getCountry().equalsIgnoreCase(""));

         return coronaVirusSummary;
      }
   }
}
