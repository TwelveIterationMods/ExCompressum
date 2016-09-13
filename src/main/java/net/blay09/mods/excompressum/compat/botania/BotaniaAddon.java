package net.blay09.mods.excompressum.compat.botania;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.IAddon;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageText;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.Iterator;

// TODO broken model
public class BotaniaAddon implements IAddon {

    public static final String SUBTILE_ORECHID_EVOLVED = ExCompressum.MOD_ID + ".orechidEvolved";
    private static final String LEXICON_ORECHID_EVOLVED = "botania.entry." + ExCompressum.MOD_ID + ".orechidEvolved";
    private static final String LEXICON_ORECHID_EVOLVED_PAGE = "botania.page." + ExCompressum.MOD_ID + ".orechidEvolved";

    public static LexiconEntry lexiconOrechidEvolved;
    public static Block runicAltar;

    private static boolean enableEvolvedOrechid;
    private static boolean disableVanillaOrechid;
    public static int manaSieveCost;
    public static int evolvedOrechidCost;
    public static int evolvedOrechidDelay;

    public BotaniaAddon(Configuration config) {
        loadConfig(config);
        BotaniaAPI.registerSubTile(SUBTILE_ORECHID_EVOLVED, SubTileOrechidEvolved.class);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void clientInit() {
        BotaniaAPIClient.registerSubtileModel(SubTileOrechidEvolved.class, new ModelResourceLocation(new ResourceLocation(Compat.BOTANIA, LibBlockNames.SUBTILE_ORECHID), "normal"));
    }

    @Override
    public void loadConfig(Configuration config) {
        enableEvolvedOrechid = config.getBoolean("Enable Evolved Orechid", "compat.botania", true, "Setting this to false will disable the Evolved Orechid.");
        disableVanillaOrechid = config.getBoolean("Disable Vanilla Orechid", "compat.botania", false, "If set to true, Botania's Orechid will not show up in the lexicon and will not be craftable.");
        manaSieveCost = config.getInt("Mana Sieve Mana Cost", "compat.botania", 1, 1, 10, "The mana cost of the Mana Sieve per Tick.");
        evolvedOrechidCost = config.getInt("Evolved Orechid Mana Cost", "compat.botania", 700, 0, 175000, "The mana cost of the Evolved Orechid. GoG Orechid is 700, vanilla Orechid is 17500.");
        evolvedOrechidDelay = config.getInt("Evolved Orechid Delay", "compat.botania", 2, 1, 1200, "The ore generation delay for the Evolved Orechid in ticks. GoG Orechid is 2, vanilla Orechid is 100.");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void postInit() {
        runicAltar = Block.REGISTRY.getObject(new ResourceLocation(Compat.BOTANIA, "runeAltar"));

        if(enableEvolvedOrechid) {
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

}
