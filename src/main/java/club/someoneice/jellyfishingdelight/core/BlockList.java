package club.someoneice.jellyfishingdelight.core;

import club.someoneice.jellyfishingdelight.block.ColorfulKrabbyPattys;
import club.someoneice.jellyfishingdelight.block.Grill;
import club.someoneice.jellyfishingdelight.block.KrabbyPatty;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BlockList {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModMain.MODID);

    public static final RegistryObject<Block> KRABBY_PATTY = BLOCKS.register("krabby_patty", KrabbyPatty::new);
    public static final RegistryObject<Block> COLORFUL_KRABBY_PATTYS = BLOCKS.register("colorful_krabby_pattys", ColorfulKrabbyPattys::new);
    public static final RegistryObject<Block> GRILL = BLOCKS.register("grill", Grill::new);
}
