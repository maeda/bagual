package io.maeda.apps.bagual.repositories;

import io.maeda.apps.bagual.models.Phishing;
import io.maeda.apps.bagual.models.Url;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PhishingRepository {

    private final UrlRepository urlRepository;

    private String phishingDataSource = "https://data.phishtank.com/data/online-valid.csv";

    private Set<Phishing> phishingData = new TreeSet<>();

    @Async
    @Scheduled(cron = "0 3,15 * * *")
    @SneakyThrows
    public void process() {
        phishingData.clear();
        FileReader phishingDataReader = getPhishingDataReader(phishingDataSource);
        phishingData = loadPhishingData(phishingDataReader);
    }

    @SneakyThrows
    public FileReader getPhishingDataReader(String url){
        log.info("Starting load phishing data from website...");
        String tmpDir = System.getProperty("java.io.tmpdir");
        URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(tmpDir + "/online-valid.csv");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        log.info("Loaded phishing data from website...");

        return new FileReader(new File(tmpDir + "/online-valid.csv"));
    }

    @SneakyThrows
    public Set<Phishing> loadPhishingData(FileReader reader) {
        Set<Phishing> data = new HashSet<>();
        CSVParser parse = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);
        long init = System.currentTimeMillis();

        log.info("Starting load phishing data processing...");
        for (CSVRecord record : parse) {
            data.add(new Phishing(record.get("url")));
            Optional<Url> url = urlRepository.findFirstByOriginalUrl(record.get("url"));
            url.map(item -> item.clone().suspect(true).build()).ifPresent(urlRepository::save);
        }
        log.info("Phishing data loading finished. Time: " + (System.currentTimeMillis() - init));

        return data;
    }

}
