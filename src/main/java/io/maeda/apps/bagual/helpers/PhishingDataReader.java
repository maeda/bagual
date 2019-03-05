package io.maeda.apps.bagual.helpers;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@Slf4j
@Component
public class PhishingDataReader {

    @Value("${io.maeda.apps.bagual.phishing-data-source:https://data.phishtank.com/data/online-valid.csv}")
    private String phishingDataSource;

    @SneakyThrows
    public Reader getPhishingDataReader(){
        log.info("Starting load phishing data from website...");
        String tmpDir = System.getProperty("java.io.tmpdir");
        URL website = new URL(phishingDataSource);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(tmpDir + "/online-valid.csv");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        log.info("Loaded phishing data from website...");

        return new FileReader(new File(tmpDir + "/online-valid.csv"));
    }
}
