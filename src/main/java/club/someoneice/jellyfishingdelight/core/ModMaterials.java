package club.someoneice.jellyfishingdelight.core;

import blueduck.jellyfishing.registry.ModBlocks;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public final class ModMaterials {
    public static final Tier SCRAP_METAL = getTier(ModBlocks.SCRAP_METAL.get());
    public static final Tier CHROME_METAL = getTier(ModBlocks.CHROME_METAL.get());

    private static Tier getTier(final ItemLike ingredient) {
        return new Tier() {
            public int getUses() {
                return 2000;
            }

            public float getSpeed() {
                return 2.0F;
            }

            public float getAttackDamageBonus() {
                return 4.0F;
            }

            public int getLevel() {
                return 2;
            }

            public int getEnchantmentValue() {
                return 8;
            }

            public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.of(ingredient);
            }
        };
    }
}
