package net.blay09.mods.excompressum.api;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ReloadRegistryEvent extends Event {
	private final RegistryType type;

	public ReloadRegistryEvent(RegistryType type) {
		this.type = type;
	}

	public RegistryType getRegistryType() {
		return type;
	}
}
