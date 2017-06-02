package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.IRegisterModel;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.tile.TileBait;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;

public class BlockBait extends BlockContainer implements IRegisterModel {

    public enum Type implements IStringSerializable {
        WOLF,
        OCELOT,
        COW,
        PIG,
        CHICKEN,
        SHEEP,
        SQUID,
        RABBIT,
        HORSE,
        DONKEY;

        public static Type[] values = values();

        @Nullable
        public static Type fromId(int id) {
            return id >= 0 && id < values.length ? values[id] : null;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.1, 1);
    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("variant", Type.class);

    public BlockBait() {
        super(Material.GROUND);
        setHardness(0.1f);
        setCreativeTab(ExCompressum.creativeTab);
        setRegistryName("bait");
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (int i = 0; i < Type.values.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileBait();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileBait tileEntity = (TileBait) world.getTileEntity(pos);
        if(tileEntity != null) {
            TileBait.EnvironmentalCondition environmentStatus = tileEntity.checkSpawnConditions(true);
            if (!world.isRemote) {
                ITextComponent chatComponent = new TextComponentTranslation(environmentStatus.langKey);
                chatComponent.getStyle().setColor(environmentStatus != TileBait.EnvironmentalCondition.CanSpawn ? TextFormatting.RED : TextFormatting.GREEN);
                player.sendMessage(chatComponent);
            }
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if(placer instanceof EntityPlayer) {
            TileBait tileEntity = (TileBait) world.getTileEntity(pos);
            if(tileEntity != null) {
                TileBait.EnvironmentalCondition environmentStatus = tileEntity.checkSpawnConditions(true);
                if (!world.isRemote) {
                    ITextComponent chatComponent = new TextComponentTranslation(environmentStatus.langKey);
                    chatComponent.getStyle().setColor(environmentStatus != TileBait.EnvironmentalCondition.CanSpawn ? TextFormatting.RED : TextFormatting.GREEN);
                    ((EntityPlayer) placer).sendMessage(chatComponent);
                }
            }
        }
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if(!ExCompressumConfig.disableParticles) {
            TileBait tileEntity = (TileBait) world.getTileEntity(pos);
            if (tileEntity != null && tileEntity.checkSpawnConditions(false) == TileBait.EnvironmentalCondition.CanSpawn) {
                if (rand.nextFloat() <= 0.2f) {
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat() * 0.5f, pos.getZ() + rand.nextFloat(), 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel(Item item) {
        ResourceLocation[] variants = new ResourceLocation[Type.values.length];
        for(int i = 0; i < variants.length; i++) {
            variants[i] = new ResourceLocation(ExCompressum.MOD_ID, "bait_" + Type.values[i].getName());
        }
        ModelBakery.registerItemVariants(item, variants);
        ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack itemStack) {
                Type type = itemStack.getItemDamage() >= 0 && itemStack.getItemDamage() < Type.values.length ? Type.values[itemStack.getItemDamage()] : null;
                if(type != null) {
                    return new ModelResourceLocation(new ResourceLocation(ExCompressum.MOD_ID, "bait_" + type.getName()), "inventory");
                } else {
                    return new ModelResourceLocation("missingno");
                }
            }
        });
    }
}
