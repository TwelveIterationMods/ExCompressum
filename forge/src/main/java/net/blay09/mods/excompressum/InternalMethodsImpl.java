package net.blay09.mods.excompressum;

import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.InternalMethods;
import net.blay09.mods.excompressum.registry.ExNihilo;

public class InternalMethodsImpl implements InternalMethods {

	@Override
	public ExNihiloProvider getExNihilo() {
		return ExNihilo.getInstance();
	}
}
