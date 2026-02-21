package dev.isnow.sixhundredelo.sdk.offset.loader.file.impl;

import dev.isnow.sixhundredelo.sdk.offset.loader.LoadingOffset;
import dev.isnow.sixhundredelo.sdk.offset.loader.file.OffsetFile;
import dev.isnow.sixhundredelo.sdk.offset.loader.file.url.OffsetUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralOffsetFile extends OffsetFile {
    private static final OffsetUrl GENERAL_OFFSETS_URL = new OffsetUrl("https://raw.githubusercontent.com/a2x/cs2-dumper/main/output/offsets.cs",
            "https://raw.githubusercontent.com/sezzyaep/CS2-OFFSETS/refs/heads/main/offsets.cs");

    private static final Pattern FIELD_PATTERN = Pattern.compile("public (?:const|static) nint (\\w+) = (0x[0-9A-Fa-f]+);");

    public GeneralOffsetFile() {
        super("GENERAL_OFFSETS", GENERAL_OFFSETS_URL);
    }

    @Override
    public HashMap<String, LoadingOffset> parse(final String content) {
        final Matcher matcher = FIELD_PATTERN.matcher(content);

        final HashMap<String, LoadingOffset> offsets = new HashMap<>();
        while (matcher.find())
        {
            final String name = matcher.group(1);
            final long value = Long.parseLong(matcher.group(2).substring(2), 16);

            offsets.put(name, new LoadingOffset(name, value));
        }

        return offsets;
    }
}
