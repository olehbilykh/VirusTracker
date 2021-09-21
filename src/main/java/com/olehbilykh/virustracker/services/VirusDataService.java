package com.olehbilykh.virustracker.services;

import com.olehbilykh.virustracker.models.Location;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang3.math.*;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.math.NumberUtils.*;

@Service
public class VirusDataService {
    private static final String VIRUS_DATA_SOURCE = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";

    private List<Location>  allStats = new ArrayList<>();

    //fetch data
    @PostConstruct
    //run on a schedule cron
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<Location> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_SOURCE)).build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);

        for(CSVRecord record : records){
            Location location = new Location();
            location.setState(record.get("Province/State"));
            location.setCountry(record.get("Country/Region"));
//            double lat = isParsable(record.get("Lat")) ? Double.parseDouble(record.get("Lat")) : 0 ;
//            location.setLat(lat);
            location.setLat(toDouble(record.get("Lat")));
            System.out.println(location);
            newStats.add(location);

        }
        this.allStats = newStats;
    }

}
