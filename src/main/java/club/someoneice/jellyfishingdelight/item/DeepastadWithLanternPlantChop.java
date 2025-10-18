package club.someoneice.jellyfishingdelight.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public final class DeepastadWithLanternPlantChop extends Item {
  public DeepastadWithLanternPlantChop() {
    super(new Properties()
      .food(new FoodProperties.Builder()
        .nutrition(10)
        .saturationMod(0.8f)
        .build())
      .craftRemainder(Items.BOWL));
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
    if (level.isClientSide()) {
      return super.finishUsingItem(stack, level, livingEntity);
    }

    if (level.getRandom().nextDouble() < 0.5) {
      livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20 * 20));
    }

    if (livingEntity instanceof Player player) {
      if (!player.addItem(new ItemStack(Items.BOWL))) {
        level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(),
          new ItemStack(Items.BOWL)));
      }
    }

    return super.finishUsingItem(stack, level, livingEntity);
  }
}
