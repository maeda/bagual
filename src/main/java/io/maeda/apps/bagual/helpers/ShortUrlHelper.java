package io.maeda.apps.bagual.helpers;

import io.maeda.apps.bagual.dtos.ShortUrlVO;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ShortUrlHelper {

    @Value("${io.maeda.bagual.alias:bagu.al}")
    private String defaultAlias;

    public Optional<ShortUrlVO> decompose(@NonNull String shortUrl) {
        Pattern pattern = Pattern.compile("(https?:/\\/)?(bagu\\.al)(\\/)([0-9a-zA-Z]+)$");
        Matcher matcher = pattern.matcher(shortUrl);

        return !matcher.find() ? Optional.empty() : Optional.of(new ShortUrlVO(matcher.group(2), matcher.group(4)));
    }

    public boolean isAShortenedUrl(@NonNull String shortUrl) {
        return decompose(shortUrl).isPresent();
    }
}
