package co.uk.coronavirus.services.impl;

import co.uk.coronavirus.generated.CoronaVirusSummary;
import co.uk.coronavirus.generated.Country;
import co.uk.coronavirus.services.CoronaVirusSummaryService;
import co.uk.coronavirus.services.DeathCountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class DefaultDeathCountService implements DeathCountService
{
   public static final Logger LOG = LoggerFactory.getLogger(DefaultDeathCountService.class);

   @Autowired
   private CoronaVirusSummaryService coronaVirusSummaryService;

   public int getDeathCountFor(final String countrySlug) throws IOException, InterruptedException
   {
      LOG.debug("Looking up deaths for country slug [{}]" , countrySlug);
      final CoronaVirusSummary coronaVirusSummary = coronaVirusSummaryService.getCoronaVirusSummary();
      return findDeaths(countrySlug, coronaVirusSummary);
   }

   private int findDeaths(final String country, CoronaVirusSummary deathCountForCountry)
   {
      final Optional<Country> countryDetails = deathCountForCountry.getCountries().stream()
               .filter(countryItr -> countryItr.getSlug().equalsIgnoreCase(country))
               .findFirst();

      return countryDetails.isPresent() ? countryDetails.get().getTotalDeaths() : 0;
   }
}
