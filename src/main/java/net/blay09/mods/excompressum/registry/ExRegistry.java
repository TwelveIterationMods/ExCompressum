package net.blay09.mods.excompressum.registry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class ExRegistry<T> implements IResourceManagerReloadListener {

    private final List<Exception> registryErrors = new ArrayList<>();

    private final String registryName;

    protected ExRegistry(String registryName) {
        this.registryName = registryName;
    }

    protected GsonBuilder gsonBuilder() {
        return new GsonBuilder()
                .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
                .registerTypeAdapter(LootTableProvider.class, new LootTableProvider.Serializer())
                .registerTypeAdapter(Ingredient.class, new IngredientSerializer())
                .registerTypeAdapter(ItemStack.class, new ItemStackSerializer());
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        try {
            Gson gson = gsonBuilder().create();
            reset();
            registryErrors.clear();

            for (ResourceLocation resourceLocation : resourceManager.getAllResourceLocations("excompressum_" + registryName.toLowerCase(Locale.ENGLISH),it -> it.endsWith(".json"))) {
                try (IResource resource = resourceManager.getResource(resourceLocation)) {
                    InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                    T data = gson.fromJson(reader, getDataClass());
                    if (data != null) {
                        load(data);
                    }
                } catch (Exception e) {
                    ExCompressum.logger.error("Parsing error loading Ex Compressum data file at {}", resourceLocation, e);
                    registryErrors.add(e);
                }
            }

            File configDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "excompressum");
            if (configDir.exists() || configDir.mkdirs()) {
                File configFile = new File(configDir, registryName + ".json");
                if (configFile.exists()) {
                    try (FileReader reader = new FileReader(configFile)) {
                        T data = gson.fromJson(reader, getDataClass());
                        if (data != null) {
                            load(data);
                        }
                    } catch (Exception e) {
                        ExCompressum.logger.error("Parsing error loading Ex Compressum data from {}.json", registryName, e);
                        registryErrors.add(e);
                    }
                } else {
                    try (FileWriter writer = new FileWriter(configFile)) {
                        gson.toJson(getEmptyData(), writer);
                    } catch (IOException ignored) {
                    }
                }
            }

            loadingFinished();
        } catch (Exception e) {
            ExCompressum.logger.error("Exception loading Ex Compressum data", e);
            registryErrors.add(e);
        }
    }

    protected abstract void reset();

    protected abstract void load(T data);

    protected void loadingFinished() {
    }

    protected abstract Class<? extends T> getDataClass();

    protected abstract T getEmptyData();
}
