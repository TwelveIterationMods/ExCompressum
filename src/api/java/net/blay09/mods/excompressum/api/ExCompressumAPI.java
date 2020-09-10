package net.blay09.mods.excompressum.api;

/**
 * To register stuff, subscribe to the appropriate {@link net.blay09.mods.excompressum.api.ReloadRegistryEvent}.
 */
public class ExCompressumAPI {
	private static InternalMethods internalMethods;

	/**
	 * INTERNAL USE ONLY
	 */
	public static void __setupAPI(InternalMethods internalMethods) {
		ExCompressumAPI.internalMethods = internalMethods;
	}

	public static ExNihiloProvider getExNihilo() {
		return internalMethods.getExNihilo();
	}
}
