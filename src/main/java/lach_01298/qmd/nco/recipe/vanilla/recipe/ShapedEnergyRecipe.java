package lach_01298.qmd.nco.recipe.vanilla.recipe;

import lach_01298.qmd.nco.item.energy.IChargableItem;
import lach_01298.qmd.nco.tile.internal.energy.EnergyStorage;
import lach_01298.qmd.nco.util.NBTHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;

public class ShapedEnergyRecipe extends ShapedOreRecipe {
	
	public ShapedEnergyRecipe(ResourceLocation group, @Nonnull ItemStack result, Object... recipe) {
		super(group, result, recipe);
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}
	
	@Override
	@Nonnull
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
		ItemStack result = output.copy();
		if (result.getItem() instanceof IChargableItem item) {
			long energy = 0L;
			for (int i = 0; i < inv.getSizeInventory(); ++i) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack.getItem() instanceof IChargableItem) {
					energy += ((IChargableItem) stack.getItem()).getEnergyStored(stack);
				}
			}
			
			new EnergyStorage(item.getMaxEnergyStored(result), item.getMaxTransfer(result), energy).writeToNBT(NBTHelper.getStackNBT(result), "energyStorage");
		}
		return result;
	}
}
