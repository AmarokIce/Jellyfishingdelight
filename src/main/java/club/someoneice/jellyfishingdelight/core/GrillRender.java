package club.someoneice.jellyfishingdelight.core;

import club.someoneice.jellyfishingdelight.block.GrillTile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import vectorwing.farmersdelight.common.block.StoveBlock;

public class GrillRender implements BlockEntityRenderer<GrillTile>,
  BlockEntityRendererProvider<GrillTile> {

  public GrillRender(BlockEntityRendererProvider.Context context) {}

  private final NonNullList<ItemStack> items = NonNullList.withSize(6, ItemStack.EMPTY);

  private final Vec2[] OFFSETS = {
    new Vec2(0.3F, 0.2F),
    new Vec2(0.0F, 0.2F),
    new Vec2(-0.3F, 0.2F),
    new Vec2(0.3F, -0.2F),
    new Vec2(0.0F, -0.2F),
    new Vec2(-0.3F, -0.2F)
  };

  @Override
  public void render(GrillTile grillTile, float v, PoseStack poseStack,
                     MultiBufferSource multiBufferSource, int i, int i1) {
    for (int i2 = 0; i2 < grillTile.container.getSlots(); i2++) {
      ItemStack itemStack = grillTile.container.getStackInSlot(i2);
      items.set(i2, itemStack);
    }

    Direction direction = grillTile.getBlockState().getValue(StoveBlock.FACING).getOpposite();

    for (int it = 0; it < items.size(); it++) {
      ItemStack itemStack = items.get(it);
      if (itemStack.isEmpty()) {
        continue;
      }

      poseStack.pushPose();
      poseStack.translate(0.5F, 1.02, 0.5F);
      float f = -direction.toYRot();
      poseStack.mulPose(Axis.YP.rotationDegrees(f));
      poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
      Vec2 itemOffset = OFFSETS[it];
      poseStack.translate(itemOffset.x, itemOffset.y, 0.0F);
      poseStack.scale(0.375F, 0.375F, 0.375F);
      if (grillTile.getLevel() != null) {
        Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.FIXED,
          LevelRenderer.getLightColor(grillTile.getLevel(), grillTile.getBlockPos().above()),
          i1, poseStack, multiBufferSource, grillTile.getLevel(),
          (int) (grillTile.getBlockPos().asLong() + i));
      }
      poseStack.popPose();
    }
  }

  @Override
  public BlockEntityRenderer<GrillTile> create(Context context) {
    return this;
  }
}
