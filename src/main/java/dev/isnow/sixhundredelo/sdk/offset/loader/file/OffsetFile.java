package dev.isnow.sixhundredelo.sdk.offset.loader.file;

import dev.isnow.sixhundredelo.sdk.offset.loader.LoadingOffset;
import dev.isnow.sixhundredelo.sdk.offset.loader.file.url.OffsetUrl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class OffsetFile {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    private final String offsetsDllName;
    private final OffsetUrl offsetUrl;

    public abstract HashMap<String, LoadingOffset> parse(final String content);

    @SneakyThrows
    public String downloadContent(final boolean alternative) {
        final HttpRequest request = HttpRequest.newBuilder().GET().uri(new URI(alternative ? offsetUrl.getAlternative() : offsetUrl.getUrl())).build();

        final HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


}
