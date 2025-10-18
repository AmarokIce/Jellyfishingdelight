package club.someoneice.jellyfishingdelight.mixin;

import blueduck.jellyfishing.item.BubbleWandItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BubbleWandItem.class)
public abstract class BubbleItemMixin implements IForgeItem {
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
