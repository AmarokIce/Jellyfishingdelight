package club.someoneice.jellyfishingdelight.block;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.compress.utils.Lists;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.function.Supplier;

public final class KrabbyPatty extends Block implements SimpleWaterloggedBlock {
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 3);

  private static final VoxelShape BOX_0 = box(2, 0, 8, 8, 10, 14);
  private static final VoxelShape BOX_1 = box(8, 0, 8, 14, 10, 14);
  private static final VoxelShape BOX_2 = box(8, 0, 2, 14, 10, 8);
  private static final VoxelShape BOX_3 = box(2, 0, 2, 8, 10, 8);
  private static final VoxelShape[] SHAPES = {BOX_1, BOX_2, BOX_3};

  private final Supplier<Item> burger;
  private final Supplier<Item> mini_burger;

  public KrabbyPatty(Supplier<Item> burger, Supplier<Item> mini_burger) {
    super(Properties.copy(Blocks.CAKE).noOcclusion());
    this.registerDefaultState(this.defaultBlockState()
      .setValue(WATERLOGGED, false)
      .setValue(BITES, 0)
    );
    this.burger = burger;
    this.mini_burger = mini_burger;
  }

  private static void onBiteOrCut(Level world, BlockState state, BlockPos pos) {
    int bites = state.getValue(BITES) + 1;
    if (bites > 3) {
      world.removeBlock(pos, false);
    }
    world.setBlock(pos, state.setValue(BITES, bites), 2);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext pContext) {
    Level level = pContext.getLevel();
    FluidState fluid = level.getFluidState(pContext.getClickedPos());
    return super.getStateForPlacement(pContext)
      .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    pBuilder.add(WATERLOGGED, BITES);
  }

  @Override
  public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                               Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
    if (pLevel.isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    final var holdItem = pPlayer.getItemInHand(pHand);
    if (!holdItem.is(ModTags.KNIVES)) {
      pPlayer.eat(pLevel, mini_burger.get().getDefaultInstance());
      onBiteOrCut(pLevel, pState, pPos);
      return InteractionResult.SUCCESS;
    }

    final var stack = new ItemStack(this.mini_burger.get());
    holdItem.setDamageValue(holdItem.getDamageValue() + 1);
    if (holdItem.getDamageValue() > holdItem.getMaxDamage()) {
      stack.shrink(1);
      pPlayer.awardStat(Stats.ITEM_BROKEN.get(holdItem.getItem()));
    }

    final var entity = new ItemEntity(pLevel,
      pPos.getX(), pPos.getY() + 0.5, pPos.getZ(), stack);
    entity.setDefaultPickUpDelay();
    pLevel.addFreshEntity(entity);

    onBiteOrCut(pLevel, pState, pPos);
    return InteractionResult.SUCCESS;
  }

  @Override
  public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
                                     boolean willHarvest, FluidState fluid) {
    final var flag = super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    if (!flag || player.isCreative()) {
      return flag;
    }

    int bites = state.getValue(BITES);
    final ItemStack stack;
    if (!player.getItemInHand(player.getUsedItemHand()).is(ModTags.KNIVES)
      && bites == 0) {
      stack = burger.get().getDefaultInstance();
    } else {
      stack = mini_burger.get().getDefaultInstance();
      stack.setCount(4 - bites);
    }
    final ItemEntity entity = new ItemEntity(level,
      pos.getX() + 0.5,
      pos.getY() + 0.5,
      pos.getZ() + 0.5,
      stack);
    entity.setDefaultPickUpDelay();
    level.addFreshEntity(entity);

    return true;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos,
                             CollisionContext pContext) {
    final var bites = pState.getValue(BITES);
    final var arrayOf = Lists.newArrayList();
    for(int i = 0; i < 3 - bites; i++) {
      arrayOf.add(SHAPES[i]);
    }

    return Shapes.or(BOX_0, arrayOf.toArray(new VoxelShape[0]));
  }

  @Override
  public Item asItem() {
    return this.burger.get();
  }

  @Override
  public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
    return this.burger.get().getDefaultInstance();
  }

  @Override
  public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level,
                                     BlockPos pos, Player player) {
    return this.burger.get().getDefaultInstance();
  }
}
