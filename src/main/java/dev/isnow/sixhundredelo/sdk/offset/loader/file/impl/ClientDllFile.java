package dev.isnow.sixhundredelo.sdk.offset.loader.file.impl;

import dev.isnow.sixhundredelo.sdk.offset.loader.LoadingOffset;
import dev.isnow.sixhundredelo.sdk.offset.loader.file.OffsetFile;
import dev.isnow.sixhundredelo.sdk.offset.loader.file.url.OffsetUrl;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientDllFile extends OffsetFile {
    private static final OffsetUrl CLIENT_DLL_URL = new OffsetUrl("https://raw.githubusercontent.com/a2x/cs2-dumper/main/output/client_dll.cs",
            "https://raw.githubusercontent.com/sezzyaep/CS2-OFFSETS/refs/heads/main/client_dll.cs");

    private static final Pattern FIELD_PATTERN = Pattern.compile("public (?:const|static) nint (\\w+) = (0x[0-9A-Fa-f]+);");

    public ClientDllFile() {
        super("CLIENT_DLL", CLIENT_DLL_URL);
    }

    @Override
    public HashMap<String, LoadingOffset> parse(final String content) {
        final HashMap<String, LoadingOffset> offsets = new HashMap<>(parseClassOffsets(content, "C_BaseEntity"));

        final Matcher matcher = FIELD_PATTERN.matcher(content);

        while (matcher.find())
        {
            final String name = matcher.group(1);
            final long value = Long.parseLong(matcher.group(2).substring(2), 16);

            offsets.put(name, new LoadingOffset(name, value));
        }

        return offsets;
    }

    private HashMap<String, LoadingOffset> parseClassOffsets(String content, String className) {
        final String classPattern = "public\\s+static\\s+class\\s+" + className + "\\s*\\{([\\s\\S]*?)\\}";
        final Pattern classRegex = Pattern.compile(classPattern, Pattern.MULTILINE);
        final Matcher classMatch = classRegex.matcher(content);

        if (!classMatch.find()) {
            System.out.println("[OFFSET FINDER] Could Not Find Class " + className);
            return null;
        }

        final HashMap<String, LoadingOffset> offsets = new HashMap<>();

        final Matcher matches = FIELD_PATTERN.matcher(classMatch.group(1));
        while (matches.find()) {
            final String name = matches.group(1);
            final long value = Long.parseLong(matches.group(2).substring(2), 16);

            offsets.put(name, new LoadingOffset(name, value));
        }

        return offsets;
    }
}