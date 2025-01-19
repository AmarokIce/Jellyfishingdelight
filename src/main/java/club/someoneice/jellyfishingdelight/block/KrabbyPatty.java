package club.someoneice.jellyfishingdelight.block;

import blueduck.jellyfishing.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public final class KrabbyPatty extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public KrabbyPatty() {
        super(Properties.copy(Blocks.CAKE).noOcclusion());
        this.registerDefaultState(this.defaultBlockState()
                .setValue(WATERLOGGED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Level level = pContext.getLevel();
        FluidState fluid = level.getFluidState(pContext.getClickedPos());
        return super.getStateForPlacement(pContext).setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(WATERLOGGED);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            pPlayer.eat(pLevel, ModItems.KRABBY_PATTY.get().getDefaultInstance());
            pLevel.removeBlock(pPos, false);

            pLevel.sendBlockUpdated(pPos, pLevel.getBlockState(pPos), pState, 3);
            pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pState));
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        var drops = super.getDrops(pState, pParams);
        drops.add(new ItemStack(ModItems.KRABBY_PATTY.get()));
        return drops;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return box(1f, 0.0f, 1f, 15f, 6, 15f);
    }

    @Override
    public Item asItem() {
        return ModItems.KRABBY_PATTY.get();
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return ModItems.KRABBY_PATTY.get().getDefaultInstance();
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return ModItems.KRABBY_PATTY.get().getDefaultInstance();
    }
}
