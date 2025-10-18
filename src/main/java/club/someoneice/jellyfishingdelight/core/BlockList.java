package club.someoneice.jellyfishingdelight.core;

import blueduck.jellyfishing.registry.ModItems;
import club.someoneice.jellyfishingdelight.block.ColorfulKrabbyPattys;
import club.someoneice.jellyfishingdelight.block.Grill;
import club.someoneice.jellyfishingdelight.block.KrabbyPatty;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BlockList {
  public static final DeferredRegister<Block> BLOCKS =
    DeferredRegister.create(ForgeRegistries.BLOCKS, ModMain.MODID);

  public static final RegistryObject<Block> KRABBY_PATTY =
    BLOCKS.register("krabby_patty", () -> new KrabbyPatty(
      ModItems.KRABBY_PATTY,
      ItemList.MINI_KRABBY_PATTY));
  public static final RegistryObject<Block> JELLY_KRABBY_PATTY =
    BLOCKS.register("jelly_krabby_patty",
      () -> new KrabbyPatty(ItemList.JELLY_KRABBY_PATTY,
        ItemList.MINI_JELLY_KRABBY_PATTY));
  public static final RegistryObject<Block> BLUE_JELLY_KRABBY_PATTY =
    BLOCKS.register("blue_jelly_krabby_patty", () -> new KrabbyPatty(
      ItemList.BLUE_JELLY_KRABBY_PATTY,
      ItemList.MINI_BLUE_JELLY_KRABBY_PATTY));
  public static final RegistryObject<Block> COLORFUL_KRABBY_PATTYS = BLOCKS.register(
    "colorful_krabby_pattys", ColorfulKrabbyPattys::new);
  public static final RegistryObject<Block> GRILL = BLOCKS.register("grill", Grill::new);
}
