package co.uk.coronavirus.services;

import co.uk.coronavirus.generated.CoronaVirusSummary;

import java.io.IOException;

public interface CoronaVirusSummaryService
{
   CoronaVirusSummary getCoronaVirusSummary() throws IOException, InterruptedException;

}
