package club.someoneice.jellyfishingdelight.mixin;

import club.someoneice.jellyfishingdelight.core.BlockList;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.Objects;
import java.util.Optional;

@Mixin(SkilletBlockEntity.class)
public abstract class SkilletMixin {
    @Shadow(remap = false) private int cookingTime;
    @Shadow(remap = false) private int cookingTimeTotal;
    @Shadow(remap = false) private int fireAspectLevel;

    @Shadow(remap = false) protected abstract Optional<CampfireCookingRecipe> getMatchingRecipe(Container recipeWrapper);

    @Shadow(remap = false) protected abstract void cookAndOutputItems(ItemStack cookingStack, Level level);

    @Inject(method = "cookingTick", at = @At("HEAD"), remap = false, cancellable = true)
    private static void reCookingTick(Level level, BlockPos pos, BlockState state, SkilletBlockEntity thiz, CallbackInfo ci) {
        var world = thiz.getLevel();
        var opt = thiz.getBlockState().getOptionalValue(SkilletBlock.WATERLOGGED);
        if (Objects.isNull(world)
                || opt.isEmpty()
                || !opt.get()
                || !world.getBlockState(thiz.getBlockPos().below()).is(BlockList.GRILL.get())) {
            return;
        }

        ((SkilletMixin) (Object) thiz).jellyfishingdelight$cookingTimeIn(thiz);
        ci.cancel();
    }

    @Unique
    private void jellyfishingdelight$cookingTimeIn(SkilletBlockEntity thiz) {
        ItemStack cookingStack = thiz.getStoredStack();
        if (cookingStack.isEmpty()) {
            this.cookingTime = 0;
        } else {
            this.cookAndOutputItems(cookingStack, thiz.getLevel());
        }
    }

    @Inject(method = "addItemToCook", at = @At("HEAD"), remap = false, cancellable = true)
    public void reAddItemToCook(ItemStack addedStack, final Player player, final CallbackInfoReturnable<ItemStack> cir) {
        SkilletBlockEntity thiz = (SkilletBlockEntity)(Object)this;

        var opt = thiz.getBlockState().getOptionalValue(SkilletBlock.WATERLOGGED);

        var world = thiz.getLevel();
        if (Objects.isNull(world)
                || opt.isEmpty()
                || !opt.get()
                || !world.getBlockState(thiz.getBlockPos().below()).is(BlockList.GRILL.get())) {
            return;
        }


        Optional<CampfireCookingRecipe> recipe = this.getMatchingRecipe(new SimpleContainer(addedStack));
        if (recipe.isEmpty()) {
            player.displayClientMessage(TextUtils.getTranslation("block.skillet.invalid_item"), true);
            cir.setReturnValue(addedStack);
        }

        this.cookingTimeTotal = SkilletBlock.getSkilletCookingTime(recipe.get().getCookingTime(), this.fireAspectLevel);
        ItemStack itemIn = thiz.getStoredStack();
        if (itemIn.isEmpty()) {
            thiz.getInventory().insertItem(0, addedStack.copy(), false);
            addedStack.setCount(0);
            world.playSound(null,
                    thiz.getBlockPos().getX() + 0.5F,
                    thiz.getBlockPos().getY() + 0.5F,
                    thiz.getBlockPos().getZ() + 0.5F,
                    ModSounds.BLOCK_SKILLET_ADD_FOOD.get(),
                    SoundSource.BLOCKS, 0.8F, 1.0F);
        }

        if (!ItemStack.matches(itemIn, addedStack) || itemIn.getCount() >= itemIn.getMaxStackSize()) {
            cir.setReturnValue(addedStack);
        }

        thiz.getInventory().insertItem(0, addedStack.copy(), false);
        addedStack.setCount(addedStack.getCount() - (addedStack.getMaxStackSize() - itemIn.getCount()));

        cir.setReturnValue(addedStack);
    }
}
