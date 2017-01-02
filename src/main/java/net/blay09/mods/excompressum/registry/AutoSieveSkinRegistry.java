package net.blay09.mods.excompressum.registry;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
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
		if (!ExCompressumConfig.skipAutoSieveSkins) {
			availableSkins.clear();
			Thread loadAutoSieveSkins = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						URL remoteURL = new URL("http://blay09.net/mods/control-panel/api/skins.php");
						InputStream in = remoteURL.openStream();
						Gson gson = new Gson();
						JsonReader reader = new JsonReader(new InputStreamReader(in));
						JsonObject root = gson.fromJson(reader, JsonObject.class);
						if (root.has("error")) {
							logger.error("Could not load remote skins for auto sieve: {}", root.get("error").getAsString());
							return;
						}
						if (root.has("skins")) {
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
			});
			loadAutoSieveSkins.run();
		}
	}

	public static String getRandomSkin() {
		if (availableSkins.isEmpty()) {
			availableSkins.add("Steve");
		}
		return availableSkins.get(random.nextInt(availableSkins.size()));
	}

}
