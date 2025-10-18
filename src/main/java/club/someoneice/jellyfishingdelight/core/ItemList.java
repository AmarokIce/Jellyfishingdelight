package club.someoneice.jellyfishingdelight.core;

import club.someoneice.jellyfishingdelight.item.Deepasta;
import club.someoneice.jellyfishingdelight.item.DeepastadWithLanternPlantChop;
import club.someoneice.jellyfishingdelight.item.ItemStinkyPatty;
import club.someoneice.jellyfishingdelight.item.Knife;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("unused")
public final class ItemList {
  public static final DeferredRegister<Item> ITEMS =
    DeferredRegister.create(ForgeRegistries.ITEMS, ModMain.MODID);

  public static final RegistryObject<Item> LANTERN_PLANT_STICK =
    ITEMS.register("lantern_plant_stick", () ->
      simpleFood(10, 0.4f, Items.STICK.getDefaultInstance()));
  public static final RegistryObject<Item> CORAL_PLANT_STICK =
    ITEMS.register("coral_plant_stick", () ->
      simpleFood(10, 0.4f, Items.STICK.getDefaultInstance()));
  public static final RegistryObject<Item> TUBE_PLANT_STICK =
    ITEMS.register("tube_plant_stick", () ->
      simpleFood(10, 0.4f, Items.STICK.getDefaultInstance()));
  public static final RegistryObject<Item> CHOPED_SEANUT =
    ITEMS.register("choped_seanut", () ->
      simpleFood(2, 0.1f));
  public static final RegistryObject<Item> CHOPED_TUBE_PLANT =
    ITEMS.register("choped_tube_plant", () ->
      simpleFood(2, 0.1f));
  public static final RegistryObject<Item> CHOPED_LANTERN_PLANT =
    ITEMS.register("choped_lantern_plant", () ->
      simpleFood(2, 0.1f));
  public static final RegistryObject<Item> CHILDREN_KRABBY_PATTY =
    ITEMS.register("children_krabby_patty", () ->
      simpleFood(18, 2.0f));
  public static final RegistryObject<Item> MINI_KRABBY_PATTY =
    ITEMS.register("mini_krabby_patty", () ->
      simpleFood(4, 0.4f, true, false));
  public static final RegistryObject<Item> JELLY_KRABBY_PATTY =
    ITEMS.register("jelly_krabby_patty", () ->
      simpleFood(14, 2.0f,
        new MobEffectInstance(MobEffects.WATER_BREATHING, 20 * 60 * 6, 0)));
  public static final RegistryObject<Item> MINI_JELLY_KRABBY_PATTY =
    ITEMS.register("mini_jelly_krabby_patty", () ->
      simpleFood(5, 0.5f, true, false,
        new MobEffectInstance(MobEffects.WATER_BREATHING, 20 * 60, 0)));
  public static final RegistryObject<Item> BLUE_JELLY_KRABBY_PATTY =
    ITEMS.register("blue_jelly_krabby_patty", () ->
      simpleFood(14, 2.0f,
        new MobEffectInstance(MobEffects.WATER_BREATHING, 20 * 60 * 6, 0)));
  public static final RegistryObject<Item> MINI_BLUE_JELLY_KRABBY_PATTY =
    ITEMS.register("mini_blue_jelly_krabby_patty", () ->
      simpleFood(5, 0.5f, true, false,
        new MobEffectInstance(MobEffects.WATER_BREATHING, 20 * 60, 0)));
  public static final RegistryObject<Item> BUBBLE_KRABBY_PATTY =
    ITEMS.register("bubble_krabby_patty", () ->
      simpleFood(6, 1.2f, false, true,
        new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 60 * 6, 0),
        new MobEffectInstance(MobEffects.WATER_BREATHING, 20 * 60 * 6, 0)));
  public static final RegistryObject<Item> RED_KRABBY_PATTY =
    ITEMS.register("red_krabby_patty", () ->
      simpleFood(4, 0.4f, true, true,
        new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 30, 0)));
  public static final RegistryObject<Item> ORANGE_KRABBY_PATTY =
    ITEMS.register("orange_krabby_patty", () ->
      simpleFood(4, 0.4f, true, true,
        new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * 30, 0)));
  public static final RegistryObject<Item> YELLOW_KRABBY_PATTY =
    ITEMS.register("yellow_krabby_patty", () ->
      simpleFood(4, 0.4f, true, true,
        new MobEffectInstance(MobEffects.DIG_SPEED, 20 * 30, 0)));
  public static final RegistryObject<Item> GREEN_KRABBY_PATTY =
    ITEMS.register("green_krabby_patty", () ->
      simpleFood(4, 0.4f, true, true,
        new MobEffectInstance(MobEffects.LUCK, 20 * 30, 0)));
  public static final RegistryObject<Item> BLUE_KRABBY_PATTY =
    ITEMS.register("blue_krabby_patty", () ->
      simpleFood(4, 0.4f, true, true,
        new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 30, 0)));
  public static final RegistryObject<Item> PURPLE_KRABBY_PATTY =
    ITEMS.register("purple_krabby_patty", () ->
      simpleFood(4, 0.4f, true, true,
        new MobEffectInstance(MobEffects.REGENERATION, 20 * 30, 0)));
  public static final RegistryObject<Item> RAW_KRABBY_PATTY_PATTY =
    ITEMS.register("raw_krabby_patty_patty", () ->
      simpleFood(1, 0.2f));
  public static final RegistryObject<Item> KRABBY_PATTY_PATTY = ITEMS.register("krabby_patty_patty",
    () -> simpleFood(2, 0.6f));
  public static final RegistryObject<Item> STINKY_PATTY = ITEMS.register("stinky_patty",
    ItemStinkyPatty::new);
  public static final RegistryObject<Item> CHUM_BUCKET =
    ITEMS.register("chum_bucket", () ->
      simpleFood(8, 0.8f, false, false, false,
        Items.BUCKET.getDefaultInstance(),
        new MobEffectInstance(MobEffects.CONFUSION, 20 * 5, 0),
        new MobEffectInstance(MobEffects.WEAKNESS, 20 * 60 * 3, 0)));
  public static final RegistryObject<Item> CHUMSTICK =
    ITEMS.register("chumstick",
      () -> simpleFood(6, 0.4f,
      new MobEffectInstance(MobEffects.WEAKNESS, 20 * 60, 0)));
  public static final RegistryObject<Item> DEEPASTA =
    ITEMS.register("deepasta", Deepasta::new);
  public static final RegistryObject<Item> DEEPASTA_WITH_LANTERN_PLANT_CHOP =
    ITEMS.register("deepasta_with_lantern_plant_chop", DeepastadWithLanternPlantChop::new);
  public static final RegistryObject<Item> CROAL_STEW =
    ITEMS.register("croal_stew",
    () -> simpleFood(12, 0.8f, Items.BOWL.getDefaultInstance(),
      new MobEffectInstance(MobEffects.GLOWING, 20 * 30, 0),
      new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 60 * 3, 0),
      new MobEffectInstance(ModEffects.NOURISHMENT.get(), 20 * 60 * 5, 0)));
  public static final RegistryObject<Item> ROCK_STEW =
    ITEMS.register("rock_stew",
      () -> simpleFood(12, 0.8f, Items.BOWL.getDefaultInstance(),
      new MobEffectInstance(MobEffects.DARKNESS, 20 * 30, 0),
      new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 60 * 3, 0),
      new MobEffectInstance(ModEffects.NOURISHMENT.get(), 20 * 60 * 5, 0)));
  public static final RegistryObject<Item> PINEAPPLE_STICK =
    ITEMS.register("pineapple_stick",
    () -> simpleFood(6, 0.3f, Items.STICK.getDefaultInstance()));
  public static final RegistryObject<Item> PINEAPPLE_SODA =
    ITEMS.register("pineapple_soda",
    () -> simpleFood(6, 0.6f, Items.GLASS_BOTTLE.getDefaultInstance()));
  public static final RegistryObject<Item> PINEAPPLE_BUN =
    ITEMS.register("pineapple_bun",
    () -> simpleFood(8, 1.0f));
  public static final RegistryObject<Item> SEANUT_CHOCOLATE =
    ITEMS.register("seanut_chocolate",
    () -> simpleFood(8, 1.0f));
  public static final RegistryObject<Item> SCRAP_METAL_KNIFE =
    ITEMS.register("scrap_metal_knife"
    , Knife::getScrapMetal);
  public static final RegistryObject<Item> CHROME_METAL_KNIFE =
    ITEMS.register("chrome_knife",
    Knife::getChromeMetal);
  public static final RegistryObject<Item> COLORFUL_KRABBY_PATTY =
    ITEMS.register(
    "colorful_krabby_pattys",
    () -> new BlockItem(BlockList.COLORFUL_KRABBY_PATTYS.get(),
      new Item.Properties()));
  public static final RegistryObject<Item> GRILL =
    ITEMS.register("grill",
    () -> new BlockItem(BlockList.GRILL.get(), new Item.Properties()));

  private static Item simpleFood(int hunger, float saturation, MobEffectInstance... effects) {
    return simpleFood(hunger, saturation, false, false, effects);
  }

  private static Item simpleFood(int hunger, float saturation, ItemStack itemReturn,
                                 MobEffectInstance... effects) {
    return simpleFood(hunger, saturation, false, false, false, itemReturn, effects);
  }

  private static Item simpleFood(int hunger, float saturation, boolean fast, boolean alwaysEat,
                                 MobEffectInstance... effects) {
    return simpleFood(hunger, saturation, fast, alwaysEat, false, ItemStack.EMPTY, effects);
  }

  private static Item simpleFood(int hunger, float saturation, boolean fast, boolean alwaysEat,
                                 boolean isDrink,
                                 @Nullable ItemStack bowl, MobEffectInstance... effects) {
    final var builder = new FoodProperties.Builder();
    builder.nutrition(hunger).saturationMod(saturation);
    if (fast) builder.fast();
    if (alwaysEat) builder.alwaysEat();

    return new Item(new Item.Properties().food(builder.build())) {
      @Override
      @NotNull
      public ItemStack finishUsingItem(final @NotNull ItemStack stack, final @NotNull Level level,
                                       final @NotNull LivingEntity livingEntity) {
        if (level.isClientSide()) {
          return super.finishUsingItem(stack, level, livingEntity);
        }

        Arrays.stream(effects)
          .filter(Objects::nonNull)
          .map(MobEffectInstance::new)
          .forEach(livingEntity::addEffect);

        if (Objects.isNull(bowl) || bowl.isEmpty()) {
          return super.finishUsingItem(stack, level, livingEntity);
        }

        if (!(livingEntity instanceof Player player)) {
          return super.finishUsingItem(stack, level, livingEntity);
        }

        final var itemBowl = bowl.copy();
        if (!player.addItem(itemBowl)) {
          level.addFreshEntity(new ItemEntity(level,
            player.getX(),
            player.getY(),
            player.getZ(),
            itemBowl));
        }

        return super.finishUsingItem(stack, level, livingEntity);
      }

      @Override
      @NotNull
      public UseAnim getUseAnimation(@NotNull final ItemStack stack) {
        return isDrink ? UseAnim.DRINK : UseAnim.EAT;
      }

      @Override
      public boolean hasCraftingRemainingItem(@NotNull final ItemStack stack) {
        return Objects.nonNull(bowl) && !bowl.isEmpty();
      }

      @SuppressWarnings("all")
      @Override
      public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return this.hasCraftingRemainingItem(itemStack) ? bowl : ItemStack.EMPTY;
      }
    };
  }
}
