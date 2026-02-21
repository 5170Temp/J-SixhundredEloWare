package dev.isnow.sixhundredelo.sdk.offset.loader.file.url;

import lombok.Data;

@Data
public class OffsetUrl {
    private final String url;
    private final String alternative;

    public OffsetUrl(String url, String alternative) {
        this.url = url;
        this.alternative = alternative;
    }
}
