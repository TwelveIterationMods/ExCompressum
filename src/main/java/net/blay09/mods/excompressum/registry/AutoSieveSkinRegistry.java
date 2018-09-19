package net.blay09.mods.excompressum.registry;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ModConfig;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class AutoSieveSkinRegistry {

    private static final Random random = new Random();
    private static final List<String> availableSkins = Lists.newArrayList();

    public static void load() {
        if (!ModConfig.client.skipAutoSieveSkins) {
            availableSkins.clear();
            Thread loadAutoSieveSkins = new Thread(() -> {
                try {
                    URL remoteURL = new URL("http://blay09.net/mods/control-panel/api/skins_v2.php");
                    InputStream in = remoteURL.openStream();
                    Gson gson = new Gson();
                    JsonReader reader = new JsonReader(new InputStreamReader(in));
                    JsonObject root = gson.fromJson(reader, JsonObject.class);
                    if (root.has("error")) {
                        ExCompressum.logger.error("Could not load remote skins for auto sieve: {}", root.get("error").getAsString());
                        return;
                    }

                    if (root.has("skins")) {
                        JsonArray skins = root.getAsJsonArray("skins");
                        for (int i = 0; i < skins.size(); i++) {
                            JsonObject skin = skins.get(i).getAsJsonObject();
                            synchronized (availableSkins) {
                                availableSkins.add(skin.get("name").getAsString());
                            }
                        }
                    }

                    reader.close();
                } catch (Throwable e) { // Screw it, let's just be overprotective.
                    ExCompressum.logger.error("Could not load remote skins for auto sieve: ", e);
                }
            });
            loadAutoSieveSkins.start();
        }
    }

    public static String getRandomSkin() {
        synchronized (availableSkins) {
            if (availableSkins.isEmpty()) {
                availableSkins.add("Steve");
            }

            return availableSkins.get(random.nextInt(availableSkins.size()));
        }
    }

}
