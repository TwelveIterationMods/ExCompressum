package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Locale;

public class BlockCompressed extends Block {

	public static final String name = "compressed_block";
	public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

	public enum Type implements IStringSerializable {
		DUST,
		COBBLESTONE,
		GRAVEL,
		SAND,
		DIRT,
		FLINT,
		NETHER_GRAVEL,
		ENDER_GRAVEL,
		SOUL_SAND,
		NETHERRACK,
		END_STONE;

		public static final Type[] values = values();

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ENGLISH);
		}

		@Nullable
		public static Type fromId(int id) {
			return id >= 0 && id < values.length ? values[id] : null;
		}
	}

	public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("variant", Type.class);

	public BlockCompressed() {
		super(Material.ROCK);
		setUnlocalizedName(registryName.toString());
		setHardness(4f);
		setResistance(6f);
		setSoundType(SoundType.STONE);
		setCreativeTab(ExCompressum.creativeTab);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateFromMeta(int meta) {
		if(meta < 0 || meta >= Type.values.length) {
			return getDefaultState();
		}
		return getDefaultState().withProperty(VARIANT, Type.values[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(this, 1, state.getValue(VARIANT).ordinal());
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (int i = 0; i < Type.values().length; i++) {
			items.add(new ItemStack(this, 1, i));
		}
	}

}
