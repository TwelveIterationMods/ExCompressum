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

        RecipeHandlerComposting handlerComposting = new RecipeHandlerComposting();
        API.registerRecipeHandler(handlerComposting);
        API.registerUsageHandler(handlerComposting);

        RecipeHandlerBarrelProcess handlerBarrel = new RecipeHandlerBarrelProcess();
        API.registerRecipeHandler(handlerBarrel);
        API.registerUsageHandler(handlerBarrel);
    }

    @Override
    public String getName() {
        return "Ex Compressum";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
}
