package club.someoneice.jellyfishingdelight.block;

import club.someoneice.jellyfishingdelight.core.ItemList;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.function.Supplier;

public final class ColorfulKrabbyPattys extends Block implements SimpleWaterloggedBlock {
  public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, 6);
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  public static final ImmutableList<Supplier<Item>> KRABBY_PATTYS = ImmutableList.of(
    ItemList.PURPLE_KRABBY_PATTY,
    ItemList.BLUE_KRABBY_PATTY,
    ItemList.GREEN_KRABBY_PATTY,
    ItemList.YELLOW_KRABBY_PATTY,
    ItemList.ORANGE_KRABBY_PATTY,
    ItemList.RED_KRABBY_PATTY
  );

  public ColorfulKrabbyPattys() {
    super(Properties.copy(Blocks.CAKE).noOcclusion());
    this.registerDefaultState(this.defaultBlockState()
      .setValue(COLOR, 6)
      .setValue(WATERLOGGED, false));
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext pContext) {
    Level level = pContext.getLevel();
    FluidState fluid = level.getFluidState(pContext.getClickedPos());
    return super.getStateForPlacement(pContext).setValue(WATERLOGGED,
      fluid.getType() == Fluids.WATER);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    pBuilder.add(COLOR, WATERLOGGED);
  }

  @Override
  public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer,
                               InteractionHand pHand, BlockHitResult pHit) {
    if (pLevel.isClientSide()) {
      return InteractionResult.sidedSuccess(true);
    }

    int color = pState.getValue(COLOR);
    if (color == 0) {
      pPlayer.addItem(Items.BOWL.getDefaultInstance());
      pLevel.removeBlock(pPos, false);
    } else {
      pPlayer.addItem(KRABBY_PATTYS.get(--color).get().getDefaultInstance());
      pLevel.setBlock(pPos, pState.setValue(COLOR, color), Block.UPDATE_CLIENTS);
    }

    return InteractionResult.SUCCESS;
  }

  @Override
  public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
    List<ItemStack> drops = super.getDrops(pState, pParams);
    int color = pState.getValue(COLOR);
    for (int i = 0; i <= color; i++) {
      drops.add(KRABBY_PATTYS.get(i).get().getDefaultInstance());
    }
    return drops;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos,
                             CollisionContext pContext) {
    return box(1f, 0.0f, 1f, 15f, 6, 15f);
  }
}
