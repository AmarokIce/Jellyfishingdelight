package club.someoneice.jellyfishingdelight.core;

import blueduck.jellyfishing.registry.ModBlocks;
import blueduck.jellyfishing.registry.ModItems;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public final class ModEvent {
  private static final Direction[] ROUND = {Direction.NORTH, Direction.SOUTH, Direction.EAST,
    Direction.WEST};
  private static final ImmutableList<Supplier<Block>> CORAL_PLANTS = ImmutableList.of(
    ModBlocks.CORAL_PLANT, ModBlocks.TUBE_PLANT, ModBlocks.LANTERN_PLANT, ModBlocks.DEEP_SPROUT
  );

  @SuppressWarnings("deprecation")
  private static void setPlant(final Level world, final BlockPos pos, final Block block,
                               final ItemStack item,
                               final boolean isCreative) {
    if (world.isClientSide()) {
      return;
    }

    if (!world.getBlockState(pos).getValue(BlockStateProperties.WATERLOGGED)) {
      return;
    }

    if (!isCreative) {
      item.shrink(1);
    }

    Arrays.stream(ROUND).forEach(it -> {
      var nPos = pos.relative(it);
      if (!world.getFluidState(nPos).is(FluidTags.WATER)) {
        return;
      }

      if (!block.canSurvive(world.getBlockState(nPos), world, nPos)) {
        return;
      }

      if (world.getRandom().nextDouble() > 0.3) {
        return;
      }

      world.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, nPos, 0);
      var state = block.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true);
      world.setBlockAndUpdate(nPos, state);
    });

    BoneMealItem.addGrowthParticles(world, pos, 15);
  }

  private static final Map<Supplier<Item>, Supplier<Block>> krabbyPattyItems = Map.of(
    ModItems.KRABBY_PATTY, BlockList.KRABBY_PATTY,
    ItemList.JELLY_KRABBY_PATTY, BlockList.JELLY_KRABBY_PATTY,
    ItemList.BLUE_JELLY_KRABBY_PATTY, BlockList.BLUE_JELLY_KRABBY_PATTY
  );

  private static void putKrabbyPatty(final Level world, final BlockPos pos, final Player player,
                                     final ItemStack item, @Nullable final Direction face,
                                     final InteractionHand hand, final BlockHitResult hit) {
    if (world.isClientSide() || Objects.isNull(face)) {
      return;
    }

    if (!player.isShiftKeyDown()) {
      return;
    }

    if (item.isEmpty()) {
      return;
    }

    final var krabbyPattyBlock = krabbyPattyItems.keySet().stream()
      .filter(it -> item.is(it.get()))
      .findFirst()
      .orElse(null);

    if (Objects.isNull(krabbyPattyBlock)) {
      return;
    }

    final Block block = krabbyPattyItems.get(krabbyPattyBlock).get();

    if (!block.isEnabled(world.enabledFeatures())) {
      return;
    }

    final BlockState state = block.getStateForPlacement(
      new BlockPlaceContext(player, hand, item, hit));
    var posIn = !world.getBlockState(pos).canBeReplaced()
      ? pos.relative(face)
      : pos;
    if (Objects.isNull(state)
      || !world.getBlockState(posIn).canBeReplaced()
      || !world.setBlock(posIn, state, 11)) {
      return;
    }

    SoundType soundtype = state.getSoundType(world, posIn, player);
    world.playSound(
      player,
      posIn,
      state.getSoundType(world, posIn, player).getPlaceSound(),
      SoundSource.BLOCKS,
      (soundtype.getVolume() + 1.0F) / 2.0F,
      soundtype.getPitch() * 0.8F);
    world.gameEvent(GameEvent.BLOCK_PLACE, posIn,
      GameEvent.Context.of(player, state));

    if (!player.getAbilities().instabuild) {
      item.shrink(1);
    }
  }

  private static void useBoneMeal(final Level world, final BlockPos pos, final ItemStack item,
                                  final Player player) {
    if (!item.is(Items.BONE_MEAL)) {
      return;
    }

    final BlockState state = world.getBlockState(pos);
    if (state.is(ModBlocks.TALL_LANTERN_PLANT.get())) {
      BoneMealItem.addGrowthParticles(world, pos, 15);
      world.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, pos, 0);
      world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
        ModBlocks.TALL_LANTERN_PLANT.get().asItem().getDefaultInstance()));
      return;
    }

    CORAL_PLANTS.stream()
      .map(Supplier::get)
      .filter(state::is)
      .findFirst()
      .ifPresent(it -> setPlant(world, pos, it, item, player.isCreative()));
  }

  @SubscribeEvent
  public static void onUsingItem(final PlayerInteractEvent.RightClickBlock event) {
    final var player = event.getEntity();
    final var world = event.getLevel();
    final var item = event.getItemStack();
    final var pos = event.getPos();

    putKrabbyPatty(world, pos, player, item, event.getFace(), event.getHand(), event.getHitVec());
    useBoneMeal(world, pos, item, player);
  }
}
