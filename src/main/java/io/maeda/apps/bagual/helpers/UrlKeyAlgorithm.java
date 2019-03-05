package io.maeda.apps.bagual.helpers;

import org.springframework.stereotype.Component;

//TODO this algorithm is not that good. Find a smarter way for url key creation.
@Component
public class UrlKeyAlgorithm {
    private String base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String generate(long seed) {
        String result = "";
        long currentSeed = seed;

        int length = base.length();

        while(currentSeed > length -1) {
            result = base.charAt(Math.toIntExact(currentSeed % length)) + result;
            currentSeed = Math.round(currentSeed / length);
        }

        return base.charAt(Math.toIntExact(currentSeed)) + result;
    }
}
