package io.maeda.apps.bagual.controllers;

import io.maeda.apps.bagual.dtos.ShorteningRequest;
import io.maeda.apps.bagual.exceptions.ShortUrlNotFoundException;
import io.maeda.apps.bagual.models.ShortUrl;
import io.maeda.apps.bagual.services.ReportService;
import io.maeda.apps.bagual.services.ShortenerService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import javax.validation.Valid;

@RestController
@RequestScope
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiController {

    private final ShortenerService service;
    private final ReportService reportService;

    @Value("${io.maeda.bagual.alias:bagu.al}")
    private String defaultAlias;

    @RequestMapping("/api.txt")
    public ResponseEntity<String> shorten(@RequestParam(value = "q") String url) {
        ShortUrl shortUrl = service.shorten(defaultAlias, url);

        return ResponseEntity.ok(shortUrl.getShortUrl());
    }

    @RequestMapping(value = "/api/shortening", method = RequestMethod.POST)
    public ResponseEntity<?> shorten(@RequestBody @Valid ShorteningRequest payload) {
        ShortUrl shortUrl = service.shorten(defaultAlias, payload.getUrl());
        return ResponseEntity.ok(SuccessResponse.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message("CREATED")
                .content(shortUrl.getShortUrl()).build());
    }

    @RequestMapping(value = "/api/details/{alias}/{shortcut}", method = RequestMethod.GET)
    public ResponseEntity<?> details(@PathVariable("alias") String alias, @PathVariable("shortcut") String shortcut) {
        SuccessResponse<Object> response = SuccessResponse.builder()
                .code(String.valueOf(HttpStatus.OK))
                .message("FOUND")
                .content(reportService.details(alias, shortcut).orElseThrow(() -> new ShortUrlNotFoundException("NOT_FOUND"))).build();

        return ResponseEntity.ok(response);
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    @Getter
    @Builder
    public static class SuccessResponse<T> {
        private String code;
        private String message;
        private T content;
    }

}
