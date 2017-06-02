package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.IRegisterModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class BlockCompressed extends Block implements IRegisterModel {

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
		setRegistryName("compressed_block");
		setUnlocalizedName(getRegistryName().toString());
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

	@Nonnull
	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(this, 1, state.getValue(VARIANT).ordinal());
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs creativeTab, NonNullList<ItemStack> list) {
		for (int i = 0; i < Type.values().length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel(Item item) {
		ResourceLocation[] variants = new ResourceLocation[Type.values.length];
		for(int i = 0; i < variants.length; i++) {
			variants[i] = new ResourceLocation(ExCompressum.MOD_ID, "compressed_" + Type.values[i].getName());
		}
		ModelBakery.registerItemVariants(item, variants);
		ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack itemStack) {
				Type type = Type.fromId(itemStack.getItemDamage());
				if(type != null) {
					return new ModelResourceLocation(new ResourceLocation(ExCompressum.MOD_ID, "compressed_" + type.getName()), "inventory");
				} else {
					return new ModelResourceLocation("missingno");
				}
			}
		});
		ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return new ModelResourceLocation(new ResourceLocation(ExCompressum.MOD_ID, "compressed_" + state.getValue(VARIANT).getName()), "normal");
			}
		});
	}

}
