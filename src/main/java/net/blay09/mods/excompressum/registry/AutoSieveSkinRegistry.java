package net.blay09.mods.excompressum.registry;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class AutoSieveSkinRegistry {

    private static final Random random = new Random();
    private static final List<String> availableSkins = Lists.newArrayList();

    public static void load() {
        availableSkins.clear();
        availableSkins.add("Runew0lf"); // Rune in a Box™
        try {
            URL remoteURL = new URL("http://blay09.net/eiranet/api/skins.php");
            InputStream in = remoteURL.openStream();
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new InputStreamReader(in));
            JsonObject root = gson.fromJson(reader, JsonObject.class);
            if(root.has("error")) {
                System.out.println("Could not load remote skins for auto sieve: " + root.get("error").getAsString());
                return;
            }
            JsonArray skins = root.getAsJsonArray("skins");
            for(int i = 0; i < skins.size(); i++) {
                JsonObject skin = skins.get(i).getAsJsonObject();
                availableSkins.add(skin.get("name").getAsString());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRandomSkin() {
        return availableSkins.get(random.nextInt(availableSkins.size()));
    }

}
