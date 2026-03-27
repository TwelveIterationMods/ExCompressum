package net.blay09.mods.excompressum.block.entity;

import net.blay09.mods.balm.platform.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.platform.energy.DefaultEnergyStorage;
import net.blay09.mods.balm.platform.energy.EnergyStorage;
import net.blay09.mods.excompressum.component.ModComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class AutoSieveBlockEntity extends AbstractAutoSieveBlockEntity implements BalmEnergyStorageProvider {

    private final DefaultEnergyStorage energyStorage = new DefaultEnergyStorage(32000) {
        @Override
        public int fill(int maxReceive, boolean simulate) {
            if (!simulate) {
                setChanged();
            }
            return super.fill(maxReceive, simulate);
        }
    };

    public AutoSieveBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public AutoSieveBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.autoSieve.value(), pos, state);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.child("EnergyStorage").ifPresent(energyStorage::deserialize);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        energyStorage.serialize(output.child("EnergyStorage"));
    }

    @Override
    public int getEnergyStored() {
        return energyStorage.getEnergy();
    }

    @Override
    public void setEnergyStored(int energy) {
        energyStorage.setEnergy(energy);
    }

    @Override
    public int getMaxEnergyStored() {
        return energyStorage.getCapacity();
    }

    @Override
    public int drainEnergy(int maxExtract, boolean simulate) {
        if (!simulate) {
            isDirty = true;
        }

        return energyStorage.drain(maxExtract, simulate);
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(ModComponents.energy.value(), energyStorage.getEnergy());
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter input) {
        super.applyImplicitComponents(input);
        final var energyComponent = input.get(ModComponents.energy.value());
        if (energyComponent != null) {
            energyStorage.setEnergy(energyComponent);
        }
    }

}
