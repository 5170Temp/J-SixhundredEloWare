package dev.isnow.sixhundredelo.sdk.offset.loader;

import dev.isnow.sixhundredelo.sdk.offset.Offsets;
import dev.isnow.sixhundredelo.sdk.offset.loader.file.OffsetFile;
import dev.isnow.sixhundredelo.sdk.offset.loader.file.impl.ClientDllFile;
import dev.isnow.sixhundredelo.sdk.offset.loader.file.impl.GeneralOffsetFile;
import dev.isnow.sixhundredelo.sdk.offset.loader.file.url.OffsetUrl;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@UtilityClass
public class OffsetLoader {

    private final OffsetUrl BUTTONS_URL = new OffsetUrl("https://raw.githubusercontent.com/a2x/cs2-dumper/main/output/buttons.cs",
                                                          "https://raw.githubusercontent.com/sezzyaep/CS2-OFFSETS/refs/heads/main/buttons.cs");

    private final OffsetUrl ENGINE2_URL = new OffsetUrl("https://raw.githubusercontent.com/a2x/cs2-dumper/main/output/engine2_dll.cs",
                                                          "https://raw.githubusercontent.com/sezzyaep/CS2-OFFSETS/refs/heads/main/engine2_dll.cs");

    // TODO: ADD REST
    private static final List<OffsetFile> offsetFiles = List.of(new GeneralOffsetFile(), new ClientDllFile());

    private static final HashMap<String, LoadingOffset> loadedOffsets = new HashMap<>();

    public void loadOffsets() {
        System.out.println("Downloading offsets");
        for (final OffsetFile offsetFile : offsetFiles) {
            final String content = offsetFile.downloadContent(false);

            loadedOffsets.putAll(offsetFile.parse(content));
            System.out.println("Loaded " + loadedOffsets.size() + " offsets from " + offsetFile.getOffsetsDllName());
        }

        updateFields();
    }

    private void updateFields() {
        System.out.println("Applying offsets with reflection...");

        for (final Field field : Offsets.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            try {
                final String fieldName = field.getName();

                if (loadedOffsets.containsKey(fieldName)) {
                    field.setAccessible(true);

                    final LoadingOffset value = loadedOffsets.get(fieldName);
                    if (value != null) {
                        field.setLong(null, value.getAddress());
                    }
                }
            } catch (final IllegalAccessException e) {
                System.err.println("Could not access field: " + field.getName());
            } catch (final IllegalArgumentException e) {
                System.err.println("Type mismatch for field: " + field.getName());
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}
