package lach_01298.qmd.nco.legacy.tile;

import java.util.List;

import lach_01298.qmd.nco.recipe.ingredient.IItemIngredient;
import lach_01298.qmd.nco.tile.inventory.ITileInventory;
import net.minecraft.item.ItemStack;

public interface IItemProcessorLegacy extends IProcessorLegacy, ITileInventory {
	
	public int getItemInputSize();
	
	public int getItemOutputSize();
	
	public List<ItemStack> getItemInputs();
	
	public List<IItemIngredient> getItemIngredients();
	
	public List<IItemIngredient> getItemProducts();
}
