package club.someoneice.jellyfishingdelight.block;

import club.someoneice.jellyfishingdelight.core.ItemList;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class ChildrenKrabbyPatty extends Block implements SimpleWaterloggedBlock {
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

  private static final VoxelShape BOX = box(5, 0, 5, 11, 7, 11);

  public ChildrenKrabbyPatty() {
    super(Properties.copy(Blocks.CAKE).noOcclusion().strength(0.1f));
    this.registerDefaultState(this.defaultBlockState()
      .setValue(FACING, Direction.NORTH)
      .setValue(WATERLOGGED, false)
    );
  }

  @Override
  public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
    return BOX;
  }

  @Override
  public List<ItemStack> getDrops(BlockState pState, Builder pParams) {
    return Lists.newArrayList(this.asItem().getDefaultInstance());
  }

  @Override
  public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level,
                                     BlockPos pos, Player player) {
    return this.asItem().getDefaultInstance();
  }

  @Override
  public Item asItem() {
    return ItemList.CHILDREN_KRABBY_PATTY.get().asItem();
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    Level level = context.getLevel();
    FluidState fluid = level.getFluidState(context.getClickedPos());
    return this.defaultBlockState()
      .setValue(FACING, context.getHorizontalDirection().getOpposite())
      .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(FACING, WATERLOGGED);
  }

  @Override
  public BlockState rotate(BlockState pState, Rotation pRot) {
    return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
  }

  @Override
  public BlockState mirror(BlockState pState, Mirror pMirror) {
    return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
  }
}
