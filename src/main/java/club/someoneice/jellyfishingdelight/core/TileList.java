package club.someoneice.jellyfishingdelight.core;

import club.someoneice.jellyfishingdelight.block.GrillTile;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TileList {
  public static final DeferredRegister<BlockEntityType<?>> TILES =
    DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ModMain.MODID);

  public static final RegistryObject<BlockEntityType<GrillTile>> GRILL = TILES.register("grill",
    () -> BlockEntityType.Builder.of(GrillTile::new, BlockList.GRILL.get()).build(null));
}
