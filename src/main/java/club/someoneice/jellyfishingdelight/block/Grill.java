package club.someoneice.jellyfishingdelight.block;

import club.someoneice.jellyfishingdelight.core.TileList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public final class Grill extends BaseEntityBlock {
  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

  public Grill() {
    super(Properties.copy(Blocks.IRON_BLOCK).strength(0.2f).requiresCorrectToolForDrops());
    this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
  }

  @Override
  public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer,
                               InteractionHand pHand, BlockHitResult pHit) {
    if (pLevel.isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    if (!(pLevel.getBlockEntity(pPos) instanceof GrillTile tile)) {
      return InteractionResult.CONSUME;
    }

    var item = pPlayer.getItemInHand(pHand);
    if (!tile.canPut(pLevel, item)) {
      return InteractionResult.FAIL;
    }

    return tile.putItem(item) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING,
      context.getHorizontalDirection().getOpposite());
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(FACING);
  }

  @Override
  public BlockState rotate(BlockState pState, Rotation pRot) {
    return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
  }

  @Override
  public BlockState mirror(BlockState pState, Mirror pMirror) {
    return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
  }

  @Override
  public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
                                     boolean willHarvest, FluidState fluid) {
    if (level.getBlockEntity(pos) instanceof GrillTile tile) {
      List<ItemStack> drops = new ArrayList<>();
      for (int i = 0; i < tile.container.getSlots(); i++) {
        drops.add(tile.container.extractItem(i, 1, false).copyAndClear());
      }

      if (player.getMainHandItem().getItem() instanceof PickaxeItem pickaxeItem) {
        if (pickaxeItem.getTier().getLevel() >= 1) {
          drops.add(this.asItem().getDefaultInstance());
        }
      }
      drops.stream()
        .map(it -> new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
          it))
        .forEach(level::addFreshEntity);
    }

    return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new GrillTile(blockPos, blockState);
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel,
                                                                          BlockState pState,
                                                                          BlockEntityType<T> pBlockEntityType) {
    return createTickerHelper(pBlockEntityType, TileList.GRILL.get(), GrillTile::cookingTick);
  }

  @Override
  public RenderShape getRenderShape(BlockState pState) {
    return RenderShape.MODEL;
  }
}
