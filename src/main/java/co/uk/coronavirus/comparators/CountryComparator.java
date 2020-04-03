package co.uk.coronavirus.comparators;

import co.uk.coronavirus.generated.Country;

import java.util.Comparator;

public class CountryComparator implements Comparator<Country>
{
   private ComparisonCriteria comparisonCriteria;

   public CountryComparator(final ComparisonCriteria comparisonCriteria)
   {
      this.comparisonCriteria = comparisonCriteria;
   }

   @Override
   public int compare(final Country country1, final Country country2)
   {
      switch (comparisonCriteria)
      {
         case TOTAL_DEATHS:
            return country1.getTotalDeaths() - country2.getTotalDeaths();
         case TOTAL_CONFIRMED:
            return country1.getTotalConfirmed() - country2.getTotalConfirmed();
         case NEW_DEATHS:
            return country1.getNewDeaths() - country2.getNewDeaths();
         case NEW_CONFIRMED:
            return country1.getNewConfirmed() - country2.getNewConfirmed();
         default:
            return 0;
      }
   }
}
