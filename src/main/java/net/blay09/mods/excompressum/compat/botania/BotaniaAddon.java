package net.blay09.mods.excompressum.compat.botania;

import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageText;

import java.util.Iterator;

public class BotaniaAddon {

    private static final String SUBTILE_ORECHID_EVOLVED = ExCompressum.MOD_ID + ".orechidEvolved";
    private static final String LEXICON_ORECHID_EVOLVED = "botania.entry." + ExCompressum.MOD_ID + ".orechidEvolved";
    private static final String LEXICON_ORECHID_EVOLVED_PAGE = "botania.page." + ExCompressum.MOD_ID + ".orechidEvolved";
    private static final String LEXICON_BROKEN_COMPRILLA = "botania.entry." + ExCompressum.MOD_ID + ".brokenComprilla";
    private static final String LEXICON_BROKEN_COMPRILLA_PAGE = "botania.page." + ExCompressum.MOD_ID + ".brokenComprilla";
    private static final String SUBTILE_BROKEN_COMPRILLA = ExCompressum.MOD_ID + ".brokenComprilla";

    public static LexiconEntry lexiconOrechidEvolved;
    public static LexiconEntry lexiconBrokenComprilla;
    public static Block runicAltar;

    @SuppressWarnings("unchecked")
    public BotaniaAddon() {
        if(ExCompressum.botaniaEvolvedOrechid) {
            BotaniaAPI.registerSubTile(SUBTILE_ORECHID_EVOLVED, SubTileOrechidEvolved.class);
            BotaniaAPI.registerSubTileSignature(SubTileOrechidEvolved.class, new SubTileOrechidEvolvedSignature());
            ItemStack orechidEvolved = ItemBlockSpecialFlower.ofType(SUBTILE_ORECHID_EVOLVED);
            ExCompressum.creativeTab.addAdditionalItem(orechidEvolved);
            RecipePetals recipeOrechidEvolved = BotaniaAPI.registerPetalRecipe(orechidEvolved, "petalGray", "petalGray", "petalYellow", "petalYellow", "petalGreen", "petalGreen", "petalRed", "petalRed");
            lexiconOrechidEvolved = new LexiconEntry(LEXICON_ORECHID_EVOLVED, BotaniaAPI.categoryFunctionalFlowers) {
                @Override
                public String getWebLink() {
                    return "http://balyware.com/index.php/ex-compressum/evolved-orechid/";
                }

                @Override
                public String getTagline() {
                    return "botania.tagline.excompressum.orechidEvolved";
                }
            };
            lexiconOrechidEvolved.setLexiconPages(new PageText(LEXICON_ORECHID_EVOLVED_PAGE + "0"), new PagePetalRecipe(LEXICON_ORECHID_EVOLVED_PAGE + "1", recipeOrechidEvolved));
            lexiconOrechidEvolved.setPriority();
            BotaniaAPI.addEntry(lexiconOrechidEvolved, lexiconOrechidEvolved.category);
        }

        if(ExCompressum.botaniaBrokenComprilla) {
            BotaniaAPI.registerSubTile(SUBTILE_BROKEN_COMPRILLA, SubTileBrokenComprilla.class);
            BotaniaAPI.registerSubTileSignature(SubTileBrokenComprilla.class, new SubTileBrokenComprillaSignature());
            ItemStack brokenComprilla = ItemBlockSpecialFlower.ofType(SUBTILE_BROKEN_COMPRILLA);
            ExCompressum.creativeTab.addAdditionalItem(brokenComprilla);
            RecipePetals recipeBrokenComprilla = BotaniaAPI.registerPetalRecipe(brokenComprilla, "petalLightBlue", "petalLightBlue", "petalWhite", "petalWhite", "petalBlack", "petalBlack", new ItemStack(Items.flint), new ItemStack(Items.flint));
            lexiconBrokenComprilla = new LexiconEntry(LEXICON_BROKEN_COMPRILLA, BotaniaAPI.categoryFunctionalFlowers) {
                @Override
                public String getWebLink() {
                    return "http://balyware.com/index.php/ex-compressum/broken-comprilla/";
                }

                @Override
                public String getTagline() {
                    return "botania.tagline.excompressum.brokenComprilla";
                }
            };
            lexiconBrokenComprilla.setLexiconPages(new PageText(LEXICON_BROKEN_COMPRILLA_PAGE + "0"), new PagePetalRecipe(LEXICON_BROKEN_COMPRILLA_PAGE + "1", recipeBrokenComprilla));
            lexiconBrokenComprilla.setPriority();
            BotaniaAPI.addEntry(lexiconBrokenComprilla, lexiconBrokenComprilla.category);
        }

        if(ExCompressum.botaniaDisableVanillaOrechid) {
            Iterator<LexiconEntry> it = BotaniaAPI.getAllEntries().iterator();
            while(it.hasNext()) {
                if(it.next().getUnlocalizedName().equals("botania.entry.orechid")) {
                    it.remove();
                    break;
                }
            }
            it = BotaniaAPI.categoryFunctionalFlowers.entries.iterator();
            while(it.hasNext()) {
                if(it.next().getUnlocalizedName().equals("botania.entry.orechid")) {
                    it.remove();
                    break;
                }
            }
            Iterator<RecipePetals> it2 = BotaniaAPI.petalRecipes.iterator();
            ItemStack itemStackOrechid = ItemBlockSpecialFlower.ofType("orechid");
            while(it2.hasNext()) {
                ItemStack output = it2.next().getOutput();
                if(ItemStack.areItemStacksEqual(itemStackOrechid, output)) {
                    it2.remove();
                    break;
                }
            }
        }

        runicAltar = GameRegistry.findBlock("Botania", "runeAltar");
    }
}
