package net.blay09.mods.excompressum.compat.botania;

import net.blay09.mods.excompressum.config.ExCompressumConfig;

public class BotaniaAddon {

    public BotaniaAddon() {
        // TODO add to functiona lflowers tag (block/item, normal/floating)
        if(ExCompressumConfig.COMMON.enableEvolvedOrechid.get()) {
            // TODO RecipePetals recipeOrechidEvolved = BotaniaAPI.registerPetalRecipe(orechidEvolved, "petalGray", "petalGray", "petalYellow", "petalYellow", "petalGreen", "petalGreen", "petalRed", "petalRed");
        }

        if(ExCompressumConfig.COMMON.disableVanillaOrechid.get()) {
            // TODO rewrite this completely
        }
    }

}
