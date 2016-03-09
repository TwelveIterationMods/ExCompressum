package net.blay09.mods.excompressum.compat.iguanatweakstconstruct;

import cpw.mods.fml.common.event.FMLServerStartedEvent;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.IAddon;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Field;
import java.util.Set;

public class IguanaTweaksTConstruct implements IAddon {

    @Override
    public void loadConfig(Configuration config) {}

    @Override
    @SuppressWarnings("unchecked")
    public void postInit() {
        try {
            Class clazz = Class.forName("iguanaman.iguanatweakstconstruct.reference.Config");
            Field field = clazz.getField("excludedModTools");
            Set<String> excludedModTools = (Set<String>) field.get(null);
            excludedModTools.add(ExCompressum.MOD_ID);
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
