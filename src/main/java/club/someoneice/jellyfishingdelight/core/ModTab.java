package club.someoneice.jellyfishingdelight.core;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModMain.MODID);

    public static final RegistryObject<CreativeModeTab> MOD_TAB = TABS.register(ModMain.MODID, () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group." + ModMain.MODID))
            .icon(() -> new ItemStack(ItemList.PINEAPPLE_SODA.get()))
            .displayItems((params, output) ->
                    ItemList.ITEMS.getEntries().forEach(it -> output.accept(it.get())))
            .build()
    );
}
