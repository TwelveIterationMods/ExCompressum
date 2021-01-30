package net.blay09.mods.excompressum.api;

public class ExCompressumAPI {
    public static final String MOD_ID = "excompressum";

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
