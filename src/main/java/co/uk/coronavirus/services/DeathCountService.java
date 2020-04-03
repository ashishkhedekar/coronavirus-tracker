package co.uk.coronavirus.services;

import java.io.IOException;

public interface DeathCountService
{
   int getDeathCountFor(final String countrySlug) throws IOException, InterruptedException;
}
