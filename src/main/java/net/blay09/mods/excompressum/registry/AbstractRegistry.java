package net.blay09.mods.excompressum.registry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public abstract class AbstractRegistry {

	public static boolean registryErrors;

	protected final String registryName;
	private boolean hasChanged;

	public AbstractRegistry(String registryName) {
		this.registryName = registryName;
	}

	public void load(File configDir) {
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
			JsonObject defaults = tryGetObject(root, "defaults");
			registerDefaults(defaults);
			JsonObject custom = tryGetObject(root, "custom");
			JsonArray entries = tryGetArray(custom, "entries");
			for(int i = 0; i < entries.size(); i++) {
				JsonElement element = entries.get(i);
				if(element.isJsonObject()) {
					loadCustom(element.getAsJsonObject());
				} else {
					throw new ClassCastException("entries must be an array of json objects in " + registryName);
				}
			}

		} catch (IOException | ClassCastException e) {
			ExCompressum.logger.error("Failed to loadCustom {} registry: {}", registryName, e);
		}
		if(root != null && hasChanged) {
			try (JsonWriter jsonWriter = new JsonWriter(new FileWriter(configFile))) {
				jsonWriter.setIndent("  ");
				gson.toJson(root, jsonWriter);
			} catch (IOException e) {
				ExCompressum.logger.error("Failed to save updated {} registry: {}", registryName, e);
			}
		}
	}

	protected abstract JsonObject create();
	protected abstract void loadCustom(JsonObject entry);
	protected abstract void registerDefaults(JsonObject defaults);

	public boolean tryGetBoolean(JsonObject root, String key, boolean defaultValue) {
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

	protected int tryGetInt(JsonObject root, String key, int defaultValue) {
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

	protected float tryGetFloat(JsonObject root, String key, float defaultValue) {
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

	protected String tryGetString(JsonObject root, String key, String defaultValue) {
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

	protected JsonObject tryGetObject(JsonObject root, String key) {
		if(root.has(key)) {
			JsonElement element = root.get(key);
			if(element.isJsonObject()) {
				return element.getAsJsonObject();
			} else {
				throw new RuntimeException("Invalid configuration format: expected " + key + " to be a json object in " + registryName);
			}
		}
		JsonObject newObject = new JsonObject();
		root.add(key, newObject);
		hasChanged = true;
		return newObject;
	}

	protected JsonArray tryGetArray(JsonObject root, String key) {
		if(root.has(key)) {
			JsonElement element = root.get(key);
			if(element.isJsonArray()) {
				return element.getAsJsonArray();
			} else {
				throw new RuntimeException("Invalid configuration format: expected " + key + " to be a json array in " + registryName);
			}
		}
		JsonArray newArray = new JsonArray();
		root.add(key, newArray);
		hasChanged = true;
		return newArray;
	}

	protected int tryParseInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			throw new RuntimeException("Expected number but got '" + s + "'");
		}
	}

	protected void logUnknownItem(ResourceLocation location) {
		ExCompressum.logger.error("Unknown item '{}' in {}", location, registryName);
		registryErrors = true;
	}

	protected void logUnknownFluid(String fluidName, ResourceLocation location) {
		ExCompressum.logger.error("Unknown fluid '{}' when registering {} in {}", fluidName, location, registryName);
		registryErrors = true;
	}

	protected void logUnknownOre(ResourceLocation location) {
		ExCompressum.logger.warn("No ore dictionary entries found for {} in {}", location.getResourcePath(), registryName);
	}

}
