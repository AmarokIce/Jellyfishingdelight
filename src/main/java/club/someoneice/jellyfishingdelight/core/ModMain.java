package club.someoneice.jellyfishingdelight.core;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModMain.MODID)
public final class ModMain {
  public static final String MODID = "jellyfishingdelight";

  public ModMain(FMLJavaModLoadingContext context) {
    final IEventBus modEventBus = context.getModEventBus();

    TileList.TILES.register(modEventBus);
    BlockList.BLOCKS.register(modEventBus);
    ItemList.ITEMS.register(modEventBus);
    ModTab.TABS.register(modEventBus);

    modEventBus.register(this);
  }

  @SubscribeEvent
  public void clientRender(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(TileList.GRILL.get(), GrillRender::new);
  }
}
