package net.blay09.mods.excompressum.block.entity;

import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.menu.AutoSieveMenu;
import net.blay09.mods.excompressum.menu.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ManaSieveBlockEntity extends AbstractAutoSieveBlockEntity implements IManaReceiver {

    private int manaStored;

    public ManaSieveBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.manaSieve.get(), pos, state);
    }

    @Override
    public int getEffectiveEnergy() {
        return ExCompressumConfig.getActive().compat.manaSieveCost;
    }

    @Override
    public int getMaxEnergyStored() {
        return ExCompressumConfig.getActive().compat.manaSieveCost * 1600;
    }

    @Override
    public int getEnergyStored() {
        return manaStored;
    }

    @Override
    public int drainEnergy(int maxExtract, boolean simulate) {
        int manaExtracted = Math.min(manaStored, maxExtract);
        if (!simulate) {
            manaStored -= manaExtracted;
        }
        return manaExtracted;
    }

    @Override
    public void setEnergyStored(int energy) {
        this.manaStored = energy;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        manaStored = tag.getInt("ManaStored");
    }

    @Override
    public void balmFromClientTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("ManaStored", manaStored);
        return super.save(tag);
    }

    @Override
    public CompoundTag balmToClientTag(CompoundTag tag) {
        return save(tag);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.excompressum.mana_sieve");
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
        return new AutoSieveMenu(ModMenus.manaSieve.get(), windowId, inventory, this);
    }

    @Override
    public boolean isFull() {
        return getEnergyStored() >= getMaxEnergyStored();
    }

    @Override
    public void receiveMana(int mana) {
        setEnergyStored(Math.min(getMaxEnergyStored(), getEnergyStored() + mana));
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return !isFull();
    }

    @Override
    public int getCurrentMana() {
        return getEnergyStored();
    }

    @Override
    public SieveAnimationType getAnimationType() {
        return SieveAnimationType.MAGIC;
    }

}
