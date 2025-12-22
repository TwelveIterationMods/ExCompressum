package net.blay09.mods.excompressum.registry;

import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.storage.loot.LootTable;

public abstract class ExCompressumSerializers {

    public static StreamCodec<RegistryFriendlyByteBuf, LootTable> LOOT_TABLE_STREAM_CODEC = StreamCodec.of(ExCompressumSerializers::writeLootTable,
            ExCompressumSerializers::readLootTable);

    public static LootTable readLootTable(RegistryFriendlyByteBuf buf) {
        final var tag = ByteBufCodecs.TAG.decode(buf);
        final var ops = RegistryOps.create(NbtOps.INSTANCE, buf.registryAccess());
        return LootTable.DIRECT_CODEC.decode(ops, tag).getOrThrow().getFirst();
    }

    public static void writeLootTable(RegistryFriendlyByteBuf buf, LootTable lootTable) {
        final var ops = RegistryOps.create(NbtOps.INSTANCE, buf.registryAccess());
        final var tag = LootTable.DIRECT_CODEC.encodeStart(ops, lootTable).getOrThrow();
        ByteBufCodecs.TAG.encode(buf, tag);
    }
}
