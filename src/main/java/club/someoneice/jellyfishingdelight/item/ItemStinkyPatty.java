package club.someoneice.jellyfishingdelight.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class ItemStinkyPatty extends Item {
    public ItemStinkyPatty() {
        super(new Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).build()));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        livingEntity.setHealth(livingEntity.getHealth() / 2);
        livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20 * 60 * 2, 0));
        livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20 * 10, 0));
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
