package net.blay09.mods.excompressum.compat.botania;

import codechicken.nei.api.API;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.IAddon;
import net.blay09.mods.excompressum.compat.INEIAddon;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageText;

import java.util.Iterator;

@Optional.Interface(modid = "NotEnoughItems", iface = "net.blay09.mods.excompressum.compat.INEIAddon", striprefs = true)
public class BotaniaAddon implements IAddon, INEIAddon {

    public static final String SUBTILE_ORECHID_EVOLVED = ExCompressum.MOD_ID + ".orechidEvolved";
    private static final String LEXICON_ORECHID_EVOLVED = "botania.entry." + ExCompressum.MOD_ID + ".orechidEvolved";
    private static final String LEXICON_ORECHID_EVOLVED_PAGE = "botania.page." + ExCompressum.MOD_ID + ".orechidEvolved";
    private static final String LEXICON_BROKEN_COMPRILLA = "botania.entry." + ExCompressum.MOD_ID + ".brokenComprilla";
    private static final String LEXICON_BROKEN_COMPRILLA_PAGE = "botania.page." + ExCompressum.MOD_ID + ".brokenComprilla";
    public static final String SUBTILE_BROKEN_COMPRILLA = ExCompressum.MOD_ID + ".brokenComprilla";

    public static LexiconEntry lexiconOrechidEvolved;
    public static LexiconEntry lexiconBrokenComprilla;
    public static Block runicAltar;

    private static boolean enableEvolvedOrechid;
    private static boolean disableVanillaOrechid;
    public static int evolvedOrechidCost;
    public static int evolvedOrechidDelay;
    private static boolean enableBrokenComprilla;
    public static int brokenComprillaCost;
    public static int brokenComprillaDelay;

    @Override
    public void loadConfig(Configuration config) {
        enableEvolvedOrechid = config.getBoolean("Enable Evolved Orechid", "compat.botania", true, "Setting this to false will disable the Evolved Orechid.");
        disableVanillaOrechid = config.getBoolean("Disable Vanilla Orechid", "compat.botania", false, "If set to true, Botania's Orechid will not show up in the lexicon and will not be craftable.");
        evolvedOrechidCost = config.getInt("Evolved Orechid Mana Cost", "compat.botania", 700, 0, 175000, "The mana cost of the Evolved Orechid. GoG Orechid is 700, vanilla Orechid is 17500.");
        evolvedOrechidDelay = config.getInt("Evolved Orechid Delay", "compat.botania", 2, 1, 1200, "The ore generation delay for the Evolved Orechid in ticks. GoG Orechid is 2, vanilla Orechid is 100.");
        enableBrokenComprilla = config.getBoolean("Enable Broken Comprilla", "compat.botania", true, "Setting this to false will disable the Broken Comprilla.");
        brokenComprillaCost = config.getInt("Broken Comprilla Mana Cost", "compat.botania", 100, 0, 1000, "The mana cost of the Broken Comprilla (per operation).");
        brokenComprillaDelay = config.getInt("Broken Comprilla Delay", "compat.botania", 40, 1, 1200, "The compression delay for the Broken Comprilla in ticks.");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void postInit() {
        runicAltar = GameRegistry.findBlock("Botania", "runeAltar");

        if(enableEvolvedOrechid) {
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
            BotaniaAPI.addSubTileToCreativeMenu(SUBTILE_ORECHID_EVOLVED);
        }

        if(enableBrokenComprilla) {
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
            BotaniaAPI.addSubTileToCreativeMenu(SUBTILE_BROKEN_COMPRILLA);
        }

        if(disableVanillaOrechid) {
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
    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {}

    @Override
    public void loadNEIConfig() {
//        API.addItemListEntry(ItemBlockSpecialFlower.ofType(BotaniaAddon.SUBTILE_ORECHID_EVOLVED));
//        API.addItemListEntry(ItemBlockSpecialFlower.ofType(BotaniaAddon.SUBTILE_BROKEN_COMPRILLA));
    }
}
