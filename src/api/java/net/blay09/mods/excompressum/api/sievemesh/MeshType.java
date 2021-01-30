package net.blay09.mods.excompressum.api.sievemesh;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.excompressum.MeshType")
public enum MeshType {
    @ZenCodeType.Field
    STRING,
    @ZenCodeType.Field
    FLINT,
    @ZenCodeType.Field
    IRON,
    @ZenCodeType.Field
    DIAMOND,
    @ZenCodeType.Field
    EMERALD,
    @ZenCodeType.Field
    NETHERITE
}
