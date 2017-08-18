package net.blay09.mods.excompressum.api;

/**
 * To register stuff, subscribe to the appropriate {@link net.blay09.mods.excompressum.api.ReloadRegistryEvent}.
 */
public class ExCompressumAPI {
	/**
	 * Do not use. Internal only.
	 */
	@Deprecated
	public static IInternalMethods __internalMethods;

	public static ExNihiloProvider getExNihilo() {
		return __internalMethods.getExNihilo();
	}
}
