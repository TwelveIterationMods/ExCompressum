package net.blay09.mods.excompressum.registry;

import java.util.function.Consumer;

public class RegistryGroupInitializer<T extends RegistryOverride> {
    private final RegistryGroup group;
    private final Consumer<T> initializer;

    public RegistryGroupInitializer(RegistryGroup group, Consumer<T> initializer) {
        this.group = group;
        this.initializer = initializer;
    }

    public RegistryGroup getGroup() {
        return group;
    }

    public Consumer<T> getInitializer() {
        return initializer;
    }
}
