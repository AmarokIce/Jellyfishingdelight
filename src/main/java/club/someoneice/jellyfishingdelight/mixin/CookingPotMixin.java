package club.someoneice.jellyfishingdelight.mixin;

import club.someoneice.jellyfishingdelight.core.BlockList;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.Objects;

@Mixin(CookingPotBlockEntity.class)
public abstract class CookingPotMixin {
    @Shadow(remap = false) private int cookTime;
    @Shadow(remap = false) private int cookTimeTotal;
    @Shadow(remap = false) private ItemStack mealContainerStack;
    @Shadow(remap = false) protected abstract void ejectIngredientRemainder(ItemStack remainderStack);

    @Inject(method = "processCooking", at = @At("HEAD"), remap = false, cancellable = true)
    private void reProcessCooking(final CookingPotRecipe recipe, final CookingPotBlockEntity thiz, final CallbackInfoReturnable<Boolean> cir) {
        var world = thiz.getLevel();
        if (Objects.isNull(world)
                || world.isClientSide()
                || !thiz.getBlockState().getValue(CookingPotBlock.WATERLOGGED)
                || !world.getBlockState(thiz.getBlockPos().below()).is(BlockList.GRILL.get())) {
            return;
        }

        this.cookTimeTotal = Mth.floor(recipe.getCookTime() * 0.7);
        this.cookTime += 1;
        if (this.cookTime < this.cookTimeTotal) {
            cir.setReturnValue(false);
            return;
        }

        this.cookTime = 0;
        this.mealContainerStack = recipe.getOutputContainer();
        ItemStack resultStack = recipe.getResultItem(world.registryAccess());
        ItemStack storedMealStack = thiz.getInventory().getStackInSlot(6);

        if (storedMealStack.isEmpty()) {
            thiz.getInventory().setStackInSlot(6, resultStack.copy());
        } else if (ItemStack.isSameItem(storedMealStack, resultStack)) {
            storedMealStack.grow(resultStack.getCount());
        }

        thiz.setRecipeUsed(recipe);

        for(int i = 0; i < 6; i++) {
            ItemStack slotStack = thiz.getInventory().getStackInSlot(i);
            if (slotStack.hasCraftingRemainingItem()) {
                this.ejectIngredientRemainder(slotStack.getCraftingRemainingItem());
            } else if (CookingPotBlockEntity.INGREDIENT_REMAINDER_OVERRIDES.containsKey(slotStack.getItem())) {
                this.ejectIngredientRemainder((CookingPotBlockEntity.INGREDIENT_REMAINDER_OVERRIDES.get(slotStack.getItem())).getDefaultInstance());
            }

            if (!slotStack.isEmpty()) {
                slotStack.shrink(1);
            }
        }

        cir.setReturnValue(true);
    }
}
