package lach_01298.qmd.nco.legacy.tile;

import java.util.List;

import lach_01298.qmd.nco.recipe.ingredient.IItemIngredient;
import lach_01298.qmd.nco.tile.inventory.ITileInventory;
import net.minecraft.item.ItemStack;

public interface IItemGeneratorLegacy extends IGeneratorLegacy, ITileInventory {
	
	public int getItemInputSize();
	
	public int getItemOutputSize();
	
	public List<ItemStack> getItemInputs(boolean consumed);
	
	public List<IItemIngredient> getItemIngredients();
	
	public List<IItemIngredient> getItemProducts();
}
