package net.blay09.mods.excompressum.registry;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.blay09.mods.excompressum.ExCompressumConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class AutoSieveSkinRegistry {

    private static final Logger logger = LogManager.getLogger();

    private static final Random random = new Random();
    private static final List<String> availableSkins = Lists.newArrayList();

    public static void load() {
        availableSkins.clear();
        if(!ExCompressumConfig.skipAutoSieveSkins) {
            try {
                URL remoteURL = new URL("http://balyware.com/control-panel/api/skins.php");
                InputStream in = remoteURL.openStream();
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new InputStreamReader(in));
                JsonObject root = gson.fromJson(reader, JsonObject.class);
                if (root.has("error")) {
                    logger.error("Could not load remote skins for auto sieve: {}", root.get("error").getAsString());
                    return;
                }
                if(root.has("skins")) {
                    JsonArray skins = root.getAsJsonArray("skins");
                    for (int i = 0; i < skins.size(); i++) {
                        JsonObject skin = skins.get(i).getAsJsonObject();
                        availableSkins.add(skin.get("name").getAsString());
                    }
                }
                reader.close();
            } catch (Throwable e) { // Screw it, let's just be overprotective.
                logger.error("Could not load remote skins for auto sieve: ", e);
            }
        }
    }

    public static String getRandomSkin() {
        return availableSkins.get(random.nextInt(availableSkins.size()));
    }

}
