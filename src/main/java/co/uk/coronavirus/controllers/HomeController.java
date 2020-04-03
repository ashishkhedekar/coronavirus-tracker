package co.uk.coronavirus.controllers;

import co.uk.coronavirus.comparators.ComparisonCriteria;
import co.uk.coronavirus.comparators.CountryComparator;
import co.uk.coronavirus.generated.CoronaVirusSummary;
import co.uk.coronavirus.services.CoronaVirusSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
public class HomeController
{
   private static final SimpleDateFormat DATE_FORMAT_WS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
   private static final SimpleDateFormat DATE_FORMAT_APPLICATION = new SimpleDateFormat("dd-MM-yyyy");

   @Autowired
   private CoronaVirusSummaryService coronaVirusSummaryService;

   @GetMapping("/")
   public String home(final Model model) throws IOException, InterruptedException, ParseException
   {
      final CoronaVirusSummary coronaVirusSummary = getCoronaVirusSummaryService().getCoronaVirusSummary();
      model.addAttribute("coronaVirusSummary", coronaVirusSummary);

      coronaVirusSummary.getCountries()
               .stream().max(new CountryComparator(ComparisonCriteria.TOTAL_DEATHS))
               .ifPresent(country -> model.addAttribute("maxDeaths", String.format("Max Total Deaths - %s - %s ", country.getCountry(), country.getTotalDeaths())));

      coronaVirusSummary.getCountries()
               .stream().max(new CountryComparator(ComparisonCriteria.TOTAL_CONFIRMED))
               .ifPresent(country -> model.addAttribute("maxConfirmed", String.format("Max Total Confirmed Cases - %s - %s ", country.getCountry(), country.getTotalConfirmed())));


      coronaVirusSummary.getCountries()
               .stream().max(new CountryComparator(ComparisonCriteria.NEW_DEATHS))
               .ifPresent(country -> model.addAttribute("newDeaths", String.format("Max New Deaths - %s - %s ", country.getCountry(), country.getNewDeaths())));

      coronaVirusSummary.getCountries()
               .stream().max(new CountryComparator(ComparisonCriteria.NEW_CONFIRMED))
               .ifPresent(country -> model.addAttribute("newConfirmed", String.format("Max New Confirmed - %s - %s ", country.getCountry(), country.getNewConfirmed())));

      final String sanitizedDate = coronaVirusSummary.getDate().substring(0, 23) + "Z";
      model.addAttribute("lastUpdatedTime", DATE_FORMAT_APPLICATION.format(DATE_FORMAT_WS.parse(sanitizedDate)));
      model.addAttribute("hostAddress", InetAddress.getLocalHost().getHostAddress());

      return "home";
   }

   public CoronaVirusSummaryService getCoronaVirusSummaryService()
   {
      return coronaVirusSummaryService;
   }
}
