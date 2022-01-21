/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.kiokoCode.covid19tracker.service;

import io.kiokoCode.covid19tracker.model.CovidStats;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import io.kiokoCode.covid19tracker.repository.CovidStatusRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author DATA INTEGRATED
 */
@Service
public class CovidtrackerService {

    private final static String COVID_TRACKER_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private final CovidStatusRepository repository;

    private List<CovidStats> allStats = new ArrayList<>();

    public CovidtrackerService(CovidStatusRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * * ")
    public void fetchCovidData() throws IOException, InterruptedException {

        List<CovidStats> newStats = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(COVID_TRACKER_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println(httpResponse.body());

        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            CovidStats stats = new CovidStats();
            stats.setState(record.get("Province/State"));
            stats.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int previousDayCases = Integer.parseInt(record.get(record.size() - 2));
            stats.setLatestTotalCases(latestCases);
            stats.setDiffFromPreviousDay(latestCases - previousDayCases);
            //System.out.println(stats);
            newStats.add(stats);

            saveCovidStatusToDb(stats);
        }

        this.allStats = newStats;
    }

    public List<CovidStats> getAllStats() {
        return allStats;
    }

    public void setAllStats(List<CovidStats> allStats) {
        this.allStats = allStats;
    }

    public void saveCovidStatusToDb (CovidStats covidStats) {
        repository.save(covidStats);
    }

}
