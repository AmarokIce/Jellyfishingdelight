package club.someoneice.jellyfishingdelight.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;
import org.spongepowered.asm.mixin.Mixin;
import vectorwing.farmersdelight.common.item.KnifeItem;

@Mixin(KnifeItem.class)
public abstract class KnifeItemMixin implements IForgeItem {
  @Override
  public boolean hasCraftingRemainingItem(ItemStack stack) {
    return true;
  }

  @Override
  public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
    if (!itemStack.isDamageableItem()) {
      return itemStack.copy();
    }

    itemStack.setDamageValue(itemStack.getDamageValue() + 1);
    return itemStack.getDamageValue() >= itemStack.getMaxDamage() ? ItemStack.EMPTY :
      itemStack.copy();
  }
}
