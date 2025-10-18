package club.someoneice.jellyfishingdelight.block;

import club.someoneice.jellyfishingdelight.core.TileList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public final class GrillTile extends BlockEntity {
  public final ItemStackHandler container = new ItemStackHandler(6);
  private int[] cookingTimes = new int[6];
  private int[] cookingTimesTotal = new int[6];
  private final ItemStack[] output = new ItemStack[6];

  public GrillTile(BlockPos pPos, BlockState pBlockState) {
    super(TileList.GRILL.get(), pPos, pBlockState);
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag nbt) {
    nbt.put("container", container.serializeNBT());
    nbt.putIntArray("cookingTime", cookingTimes);
    nbt.putIntArray("cookingTimesTotal", cookingTimesTotal);

    super.saveAdditional(nbt);
  }

  @Override
  public void load(@NotNull CompoundTag nbt) {
    super.load(nbt);

    this.container.deserializeNBT(nbt.getCompound("container"));
    this.cookingTimes = nbt.getIntArray("cookingTime");
    this.cookingTimesTotal = nbt.getIntArray("cookingTimesTotal");
  }

  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public @NotNull CompoundTag getUpdateTag() {
    var nbt = super.getUpdateTag();
    this.saveAdditional(nbt);
    return nbt;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    super.handleUpdateTag(tag);
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
    return super.getCapability(cap);
  }

  @SuppressWarnings("unused")
  public static void cookingTick(final Level pLevel, final BlockPos pPos,
                                 final BlockState pBlockState, final GrillTile tile) {
    final IntList list = new IntArrayList();

    if (tile.shouldPop()) {
      return;
    }

    for (int i = 0; i < 6; i++) {
      if (!tile.container.getStackInSlot(i).isEmpty()) {
        list.add(i);
      }
    }

    final var flag = pLevel.getFluidState(pPos.above()).is(FluidTags.WATER);

    list.intStream()
      .sorted()
      .filter(it -> !tile.container.getStackInSlot(it).isEmpty())
      .forEach(it -> {
        final var recipe = getRecipe(pLevel, tile.container.getStackInSlot(it));
        assert recipe != null;
        tile.cookingTimesTotal[it] = Mth.floor(recipe.getCookingTime() * (flag ? 0.7 : 1.0));
        tile.output[it] = recipe.getResultItem(pLevel.registryAccess()).copy();

        if (++tile.cookingTimes[it] >= tile.cookingTimesTotal[it]) {
          tile.cookingTimes[it] = 0;
          tile.cookingTimesTotal[it] = 0;
          tile.container.setStackInSlot(it, ItemStack.EMPTY);

          tile.send();
          if (tile.output[it] == null || tile.output[it].isEmpty()) {
            tile.output[it] = null;
            return;
          }

          pLevel.addFreshEntity(new ItemEntity(pLevel,
            tile.getBlockPos().getX() + 0.5,
            tile.getBlockPos().getY() + 1,
            tile.getBlockPos().getZ() + 0.5,
            tile.output[it].copy()));
          tile.output[it] = null;
        }
      });
  }

  private boolean shouldPop() {
    if (!this.hasLevel()) {
      return true;
    }

    assert this.level != null;

    if (this.level.isClientSide) {
      return true;
    }

    if (!this.level.getBlockState(this.getBlockPos().above()).isAir()) {
      return !this.level.getFluidState(this.getBlockPos().above()).is(FluidTags.WATER);
    }

    return false;
  }

  public static boolean canPut(Level world, ItemStack item) {
    return Objects.nonNull(getRecipe(world, item));
  }

  public boolean putItem(ItemStack item) {
    for (int i = 0; i < this.container.getSlots(); i++) {
      var it = this.container.getStackInSlot(i);
      if (it.isEmpty()) {
        this.container.setStackInSlot(i, item.copyWithCount(1));
        item.shrink(1);

        send();
        return true;
      }
    }

    return false;
  }

  private void send() {
    if (!this.hasLevel()) {
      return;
    }

    assert this.level != null;
    if (this.level.isClientSide) {
      return;
    }

    this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    ((ServerLevel) this.level).players().forEach(it -> it.connection.send(this.getUpdatePacket()));
  }

  @Nullable
  private static CampfireCookingRecipe getRecipe(Level world, ItemStack item) {
    Optional<CampfireCookingRecipe> recipe = world.getRecipeManager()
      .getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SimpleContainer(item), world);
    return recipe.orElse(null);
  }
}
