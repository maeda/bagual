package io.maeda.apps.bagual.services;

import io.maeda.apps.bagual.helpers.PhishingDataReader;
import io.maeda.apps.bagual.models.Alias;
import io.maeda.apps.bagual.models.ShortUrl;
import io.maeda.apps.bagual.models.Url;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PhishingService {
    private final UrlService urlService;
    private final ShortUrlService shortUrlService;
    private final AliasService aliasService;
    private final PhishingDataReader phishingDataReader;

    public Optional<ShortUrl> isPhishing(String aliasName, String shortcut) {
        Alias alias = aliasService.find(aliasName);
        Optional<ShortUrl> shortUrl = shortUrlService.find(alias, shortcut);

        return shortUrl.filter(item -> item.getUrl().isSuspect());
    }

    @SneakyThrows
    @Scheduled(cron = "0 3,15 * * *")
    public void process() {
        CSVParser parse = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(phishingDataReader.getPhishingDataReader());
        long init = System.currentTimeMillis();

        log.info("Starting load phishing data processing...");
        Collection<Url> urls = new LinkedList<>();

        for (CSVRecord record : parse) {
            urlService.setUrlAsMalicious(record.get("url"))
                    .ifPresent(urls::add);
        }
        log.info("Phishing urls found: " + urls);
        log.info("Phishing data loading finished. Total: " + urls.size() + " Time: " + (System.currentTimeMillis() - init));
    }

}
