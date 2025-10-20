package club.someoneice.jellyfishingdelight.item;

import club.someoneice.jellyfishingdelight.block.FishTank;
import club.someoneice.jellyfishingdelight.core.BlockList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class FishTankItem extends BlockItem {
  public FishTankItem() {
    super(BlockList.FISH_TANK.get(), new Properties());
  }

  @Override
  protected boolean canPlace(BlockPlaceContext pContext, BlockState pState) {
    if (!super.canPlace(pContext, pState)) {
      return false;
    }

    final var pos = pContext.getClickedPos();
    final Direction facing = pState.getValue(FishTank.FACING);
    final BlockPos neighborPos = pos.relative(facing.getCounterClockWise());

    return pContext.getLevel().getBlockState(neighborPos).canBeReplaced();
  }
}
