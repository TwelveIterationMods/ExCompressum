package net.blay09.mods.excompressum.registry;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class AbstractRegistry {

	public static List<String> registryErrors = Lists.newArrayList();

	protected final String registryName;
	private boolean hasChanged;
	private boolean refuseSave;

	public AbstractRegistry(String registryName) {
		this.registryName = registryName;
	}

	public final void load(File configDir) {
		clear();
		Gson gson = new Gson();
		File configFile = new File(configDir, registryName + ".json");
		if(!configFile.exists()) {
			try(JsonWriter jsonWriter = new JsonWriter(new FileWriter(configFile))) {
				jsonWriter.setIndent("  ");
				gson.toJson(create(), jsonWriter);
			} catch (IOException e) {
				ExCompressum.logger.error("Failed to create default {} registry: {}", registryName, e);
			}
		}

		JsonObject root = null;
		try(JsonReader jsonReader = new JsonReader(new FileReader(configFile))) {
			jsonReader.setLenient(true);
			root = gson.fromJson(jsonReader, JsonObject.class);
			if(hasOptions()) {
				JsonObject options = tryGetObject(root, "options");
				loadOptions(options);
			}
			JsonObject defaults = tryGetObject(root, "defaults");
			registerDefaults(defaults);
			JsonObject custom = tryGetObject(root, "custom");
			JsonArray entries = tryGetArray(custom, "entries");
			for(int i = 0; i < entries.size(); i++) {
				JsonElement element = entries.get(i);
				if(element.isJsonObject()) {
					loadCustom(element.getAsJsonObject());
				} else {
					logError("Failed to preInit %s registry: entries must be an array of json objects", registryName);
				}
			}

		} catch (IOException | ClassCastException | JsonSyntaxException e) {
			logError("Failed to preInit %s registry: %s", registryName, e);
			refuseSave = true;
		}
		if(root != null && hasChanged && !refuseSave) {
			try (JsonWriter jsonWriter = new JsonWriter(new FileWriter(configFile))) {
				jsonWriter.setIndent("  ");
				gson.toJson(root, jsonWriter);
			} catch (IOException e) {
				ExCompressum.logger.error("Failed to save updated {} registry: {}", registryName, e);
			}
		}
		MinecraftForge.EVENT_BUS.post(new ReloadRegistryEvent(this));
	}

	protected abstract void clear();
	protected abstract JsonObject create();
	protected abstract void loadCustom(JsonObject entry);
	protected abstract void registerDefaults(JsonObject defaults);

	protected void loadOptions(JsonObject entry) {}
	protected boolean hasOptions() {
		return false;
	}

	protected final boolean tryGetBoolean(JsonObject root, String key, boolean defaultValue) {
		if(root.has(key)) {
			JsonElement element = root.get(key);
			if(element.isJsonPrimitive()) {
				return element.getAsBoolean();
			}
		}
		root.addProperty(key, defaultValue);
		hasChanged = true;
		return defaultValue;
	}

	protected final int tryGetInt(JsonObject root, String key, int defaultValue) {
		if(root.has(key)) {
			JsonElement element = root.get(key);
			if(element.isJsonPrimitive()) {
				return element.getAsInt();
			}
		}
		root.addProperty(key, defaultValue);
		hasChanged = true;
		return defaultValue;
	}

	protected final float tryGetFloat(JsonObject root, String key, float defaultValue) {
		if(root.has(key)) {
			JsonElement element = root.get(key);
			if(element.isJsonPrimitive()) {
				return element.getAsFloat();
			}
		}
		root.addProperty(key, defaultValue);
		hasChanged = true;
		return defaultValue;
	}

	protected final String tryGetString(JsonObject root, String key, String defaultValue) {
		if(root.has(key)) {
			JsonElement element = root.get(key);
			if(element.isJsonPrimitive()) {
				return element.getAsString();
			}
		}
		root.addProperty(key, defaultValue);
		hasChanged = true;
		return defaultValue;
	}

	protected final JsonObject tryGetObject(JsonObject root, String key) {
		if(root.has(key)) {
			JsonElement element = root.get(key);
			if(element.isJsonObject()) {
				return element.getAsJsonObject();
			} else {
				logError("Invalid configuration format: expected %s to be a json object in %s, but got %s", key, registryName, element.getClass().toString());
				refuseSave = true;
				return new JsonObject();
			}
		}
		JsonObject newObject = new JsonObject();
		root.add(key, newObject);
		hasChanged = true;
		return newObject;
	}

	protected final JsonArray tryGetArray(JsonObject root, String key) {
		if(root.has(key)) {
			JsonElement element = root.get(key);
			if(element.isJsonArray()) {
				return element.getAsJsonArray();
			} else {
				logError("Invalid configuration format: expected %s to be a json array in %s, but got %s", key, registryName, element.getClass().toString());
				refuseSave = true;
				return new JsonArray();
			}
		}
		JsonArray newArray = new JsonArray();
		root.add(key, newArray);
		hasChanged = true;
		return newArray;
	}

	protected final int tryParseInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			logError("Expected number but got %s, falling back to 0...", s);
			refuseSave = true;
			return 0;
		}
	}

	protected final void logError(String format, Object... args) {
		String s = String.format(format, args);
		ExCompressum.logger.error(s);
		registryErrors.add(s);
	}

	protected final void logWarning(String format, Object... args) {
		String s = String.format(format, args);
		ExCompressum.logger.error(s);
		if(ExCompressumConfig.showRegistryWarnings) {
			registryErrors.add(s);
		}
	}

	protected final void logUnknownItem(ResourceLocation location) {
		String s = String.format("Unknown item '%s' in %s", location, registryName);
		ExCompressum.logger.error(s);
		registryErrors.add(s);
	}

	protected final void logUnknownFluid(String fluidName, ResourceLocation location) {
		String s = String.format("Unknown fluid '%s' when registering %s in %s", fluidName, location, registryName);
		ExCompressum.logger.error(s);
		registryErrors.add(s);
	}

	protected final void logUnknownOre(ResourceLocation location) {
		ExCompressum.logger.warn("No ore dictionary entries found for {} in {}", location.getResourcePath(), registryName);
	}

}
