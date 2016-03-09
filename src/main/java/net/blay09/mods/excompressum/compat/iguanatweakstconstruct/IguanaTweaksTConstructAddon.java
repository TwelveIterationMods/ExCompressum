package net.blay09.mods.excompressum.compat.iguanatweakstconstruct;

import cpw.mods.fml.common.event.FMLServerStartedEvent;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.compat.IAddon;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;
import java.util.Set;

public class IguanaTweaksTConstructAddon implements IAddon {

    @Override
    public void loadConfig(Configuration config) {}

    @Override
    @SuppressWarnings("unchecked")
    public void postInit() {
        try {
            Class clazz = Class.forName("iguanaman.iguanatweakstconstruct.tweaks.IguanaTweaks");
            Field toolWhitelistField = clazz.getField("toolWhitelist");
            Set<Item> toolWhitelist = (Set<Item>) toolWhitelistField.get(null);
            toolWhitelist.add(ModItems.compressedCrook);
            toolWhitelist.add(ModItems.compressedHammerWood);
            toolWhitelist.add(ModItems.compressedHammerStone);
            toolWhitelist.add(ModItems.compressedHammerIron);
            toolWhitelist.add(ModItems.compressedHammerGold);
            toolWhitelist.add(ModItems.compressedHammerDiamond);
            toolWhitelist.add(ModItems.chickenStick);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {}
}
