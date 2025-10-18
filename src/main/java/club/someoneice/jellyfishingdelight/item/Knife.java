package club.someoneice.jellyfishingdelight.item;

import club.someoneice.jellyfishingdelight.core.ModMaterials;
import net.minecraft.world.item.Tier;
import vectorwing.farmersdelight.common.item.KnifeItem;

public final class Knife extends KnifeItem {
  public static final Knife SCRAP_METAL = new Knife(ModMaterials.SCRAP_METAL);
  public static final Knife CHROME_METAL = new Knife(ModMaterials.CHROME_METAL);

  private Knife(Tier tier) {
    super(tier, 0, 0.0f, new Properties().durability(2000));
  }

  public static Knife getScrapMetal() {
    return SCRAP_METAL;
  }

  public static Knife getChromeMetal() {
    return CHROME_METAL;
  }
}
