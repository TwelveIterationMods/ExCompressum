package net.blay09.mods.excompressum.compat.botania;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import net.blay09.mods.excompressum.registry.data.CompressedRecipe;
import net.blay09.mods.excompressum.registry.CompressedRecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.SoundCategory;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.sound.BotaniaSoundEvents;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;

import java.util.List;

public class SubTileBrokenComprilla extends SubTileFunctional {

    private static final int RANGE = 5;
    private static final int RANGE_Y = 3;

    private final Multiset<CompressedRecipe> altarItems = HashMultiset.create();
    private final List<ItemStack> restItems = Lists.newArrayList();

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (redstoneSignal > 0) {
            return;
        }

        altarItems.clear();
        restItems.clear();
        int cost = getCost();
        if (!supertile.getWorld().isRemote && mana >= cost && ticksExisted % getDelay() == 0) {
            for (int i = -RANGE; i < RANGE + 1; i++) {
                for (int j = -RANGE_Y; j < RANGE_Y; j++) {
                    for (int k = -RANGE; k < RANGE + 1; k++) {
                        int x = supertile.xCoord + i;
                        int y = supertile.yCoord + j;
                        int z = supertile.zCoord + k;
                        Block block = supertile.getWorld().getBlock(x, y, z);
                        if (block == BotaniaAddon.runicAltar) {
                            IInventory inventory = (IInventory) supertile.getWorld().getTileEntity(x, y, z);
                            if (inventory != null) {
                                for (int l = 0; l < inventory.getSizeInventory(); l++) {
                                    ItemStack itemStack = inventory.getStackInSlot(l);
                                    CompressedRecipe compressedRecipe = CompressedRecipeRegistry.getRecipe(itemStack);
                                    if (compressedRecipe != null) {
                                        altarItems.add(compressedRecipe);
                                    }
                                }
                                for (CompressedRecipe compressedRecipe : altarItems.elementSet()) {
                                    if (altarItems.count(compressedRecipe) >= compressedRecipe.getSourceStack().stackSize) {
                                        EntityItem entityItem = new EntityItem(supertile.getWorld(), supertile.getPos().getX() + 0.5, supertile.getPos().getY() + 0.5, supertile.getPos().getZ() + 0.5, compressedRecipe.getResultStack().copy());
                                        double motion = 0.1f;
                                        entityItem.motionX = motion * (Math.random() - 0.5);
                                        entityItem.motionY = 0.2 + motion * (Math.random() - 0.5);
                                        entityItem.motionZ = motion * (Math.random() - 0.5);
                                        supertile.getWorld().spawnEntityInWorld(entityItem);
                                        int count = 0;
                                        for(int l = 0; l < inventory.getSizeInventory(); l++) {
                                            ItemStack itemStack = inventory.getStackInSlot(l);
                                            inventory.setInventorySlotContents(l, null);
                                            if(itemStack != null) {
                                                if(count < compressedRecipe.getSourceStack().stackSize && itemStack.isItemEqual(compressedRecipe.getSourceStack()) && ItemStack.areItemStackTagsEqual(itemStack, compressedRecipe.getSourceStack())) {
                                                    count++;
                                                } else {
                                                    restItems.add(itemStack);
                                                }
                                            }
                                        }
                                        for(int l = 0 ; l < restItems.size(); l++) {
                                            inventory.setInventorySlotContents(l, restItems.get(l));
                                        }
                                        supertile.getWorld().playSound(supertile.getPos().getX(), supertile.getPos().getY(), supertile.getPos().getZ(), BotaniaSoundEvents.endoflame, SoundCategory.BLOCKS, 0.1f, 2f, false);
                                        VanillaPacketDispatcher.dispatchTEToNearbyPlayers((TileEntity) inventory);
                                        mana -= cost;
                                        sync();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public IIcon getIcon() {
        return SubTileBrokenComprillaSignature.icon;
    }

    @Override
    public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(toChunkCoordinates(), RANGE);
    }

    @Override
    public boolean acceptsRedstone() {
        return true;
    }

    @Override
    public int getMaxMana() {
        return getCost();
    }

    @Override
    public LexiconEntry getEntry() {
        return BotaniaAddon.lexiconBrokenComprilla;
    }

    private int getCost() {
        return BotaniaAddon.brokenComprillaCost;
    }

    private int getDelay() {
        return BotaniaAddon.brokenComprillaDelay;
    }

}
