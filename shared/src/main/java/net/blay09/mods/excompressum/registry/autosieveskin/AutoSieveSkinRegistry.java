package net.blay09.mods.excompressum.registry.autosieveskin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ExCompressumConfig;

import org.jetbrains.annotations.Nullable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutoSieveSkinRegistry {

    private static final Random random = new Random();
    private static final List<WhitelistEntry> availableSkins = new ArrayList<>();

    public static void load() {
        if (!ExCompressumConfig.getActive().client.skipAutoSieveSkins) {
            availableSkins.clear();
            Thread loadAutoSieveSkins = new Thread(() -> {
                try {
                    URL remoteURL = new URL("https://whitelist.blay09.net/api/whitelists/BlayTheNinth");
                    InputStream in = remoteURL.openStream();
                    Gson gson = new Gson();
                    JsonReader reader = new JsonReader(new InputStreamReader(in));
                    List<WhitelistEntry> result = gson.fromJson(reader, new TypeToken<List<WhitelistEntry>>() {
                    }.getType());
                    synchronized (availableSkins) {
                        availableSkins.addAll(result);
                    }
                    reader.close();
                } catch (Throwable e) { // Screw it, let's just be overprotective.
                    ExCompressum.logger.error("Could not load remote skins for auto sieve: ", e);
                }
            });
            loadAutoSieveSkins.start();
        }
    }

    @Nullable
    public static WhitelistEntry getRandomSkin() {
        synchronized (availableSkins) {
            if (availableSkins.isEmpty()) {
                return null;
            }

            return availableSkins.get(random.nextInt(availableSkins.size()));
        }
    }

}
