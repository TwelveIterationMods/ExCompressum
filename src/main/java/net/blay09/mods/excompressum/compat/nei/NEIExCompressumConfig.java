package net.blay09.mods.excompressum.compat.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIExCompressumConfig implements IConfigureNEI {
    @Override
    public void loadConfig() {
        RecipeHandlerCompressedHammer handlerHammer = new RecipeHandlerCompressedHammer();
        API.registerRecipeHandler(handlerHammer);
        API.registerUsageHandler(handlerHammer);
        RecipeHandlerHeavySieve handlerSieve = new RecipeHandlerHeavySieve();
        API.registerRecipeHandler(handlerSieve);
        API.registerUsageHandler(handlerSieve);
    }

    @Override
    public String getName() {
        return "EX Compressum";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
}
