package club.someoneice.jellyfishingdelight.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FishTank extends Block implements SimpleWaterloggedBlock {
  public static final BooleanProperty SPONGE = BooleanProperty.create("sponge");
  public static final BooleanProperty LEFT = BooleanProperty.create("left");
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static final DirectionProperty FACING =
    DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

  public static final VoxelShape BOTTOM = box(0, 0, 0, 16, 1, 16);

  public static final VoxelShape BASE_0 = box(0, 0, 15, 16, 16, 16);
  public static final VoxelShape BASE_1 = box(0, 0, 0, 1, 16, 16);
  public static final VoxelShape BASE_2 = box(0, 0, 0, 16, 16, 1);
  public static final VoxelShape BASE_3 = box(15, 0, 0, 16, 16, 16);

  public static final VoxelShape[] BOX = {BASE_0, BASE_1, BASE_2, BASE_3};

  public FishTank() {
    super(Properties.copy(Blocks.GLASS).noOcclusion().strength(1.5f));
    this.registerDefaultState(this.defaultBlockState()
      .setValue(FACING, Direction.NORTH)
      .setValue(LEFT, true)
      .setValue(SPONGE, false)
      .setValue(WATERLOGGED, false)
    );
  }

  @Override
  public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                               Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
    if (!pPlayer.getItemInHand(pHand).is(Items.SPONGE) || pLevel.isClientSide) {
      return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    final var flag = pState.getValue(LEFT);
    final Direction facing = pState.getValue(FACING);
    final var neighborPos = pPos.relative(flag
      ? facing.getCounterClockWise()
      : facing.getClockWise()
    );
    final var neighborState = pLevel.getBlockState(neighborPos);
    final var newState = pState.setValue(SPONGE, true);
    final var newNeighborState = neighborState.setValue(SPONGE, true);

    pLevel.setBlock(pPos, newState, 2);
    pLevel.setBlock(neighborPos, newNeighborState, 2);

    return InteractionResult.SUCCESS;
  }

  @Override
  public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState,
                      boolean pMovedByPiston) {
    if (!pState.getValue(LEFT)) {
      return;
    }
    final Direction facing = pState.getValue(FACING);
    final BlockPos neighborPos = pPos.relative(facing.getCounterClockWise());

    pLevel.setBlock(neighborPos, this.defaultBlockState()
        .setValue(FACING, facing)
        .setValue(LEFT, false)
        .setValue(WATERLOGGED, pState.getValue(WATERLOGGED))
        .setValue(SPONGE, pState.getValue(SPONGE))
      , 2);
  }

  @Override
  public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
    final var flag = pState.getValue(LEFT);
    final Direction facing = pState.getValue(FACING);
    final var neighborPos = pPos.relative(flag
      ? facing.getCounterClockWise()
      : facing.getClockWise()
    );
    final BlockState neighborState = pLevel.getBlockState(neighborPos);
    if (neighborState.getBlock() instanceof FishTank) {
      pLevel.removeBlock(neighborPos, false);
    }
  }

  @Override
  public List<ItemStack> getDrops(BlockState pState, Builder pParams) {
    final var list = super.getDrops(pState, pParams);
    if (pState.getValue(LEFT)) {
      list.add(this.asItem().getDefaultInstance());
    }
    return list;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    final var fluid = context.getLevel().getFluidState(context.getClickedPos());
    return this.defaultBlockState()
      .setValue(FACING, context.getHorizontalDirection().getOpposite())
      .setValue(LEFT, true)
      .setValue(SPONGE, false)
      .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(FACING, SPONGE, LEFT, WATERLOGGED);
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
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos,
                             CollisionContext pContext) {
    final Direction facing = pState.getValue(FACING);
    final int faceCutout = (pState.getValue(LEFT)
      ? facing.getCounterClockWise()
      : facing.getClockWise()).get2DDataValue();

    var copyOn = Arrays.copyOf(BOX, BOX.length);
    copyOn[faceCutout] = null;
    copyOn = Arrays.stream(copyOn)
      .filter(Objects::nonNull)
      .toArray(VoxelShape[]::new);

    return Shapes.or(BOTTOM, copyOn);
  }
}
