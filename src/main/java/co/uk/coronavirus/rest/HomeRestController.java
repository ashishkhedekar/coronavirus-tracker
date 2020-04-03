package co.uk.coronavirus.rest;

import co.uk.coronavirus.services.CountryService;
import co.uk.coronavirus.services.impl.DefaultDeathCountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/v1/coronavirus")
public class HomeRestController
{
   private static final Logger LOG = LoggerFactory.getLogger(HomeRestController.class);

   @Autowired
   private CountryService countryService;

   @Autowired
   private DefaultDeathCountService defaultDeathCountService;

   @GetMapping( value = "/country/{country}/deaths")
   public int getDeathCount(@PathVariable final String country) throws IOException, InterruptedException
   {
      LOG.debug("Requested deaths for [{}] ", country);
      if (country != null)
      {
         final String decodedCountry = URLDecoder.decode(country, StandardCharsets.UTF_8.toString());
         final String countrySlug = getCountryService().getSlugFor(decodedCountry.toUpperCase());
         LOG.debug("The slug for the country [{}] is [{}] ", country, countrySlug);
         return defaultDeathCountService.getDeathCountFor(countrySlug);
      }
      return 0;
   }

   public CountryService getCountryService()
   {
      return countryService;
   }
}
