package lach_01298.qmd.nco.legacy.tile;

import java.util.List;

import lach_01298.qmd.nco.recipe.ingredient.IFluidIngredient;
import lach_01298.qmd.nco.tile.fluid.ITileFluid;
import lach_01298.qmd.nco.tile.internal.fluid.Tank;

public interface IFluidGeneratorLegacy extends IGeneratorLegacy, ITileFluid {
	
	public int getFluidInputSize();
	
	public int getFluidOutputputSize();
	
	public List<Tank> getFluidInputs(boolean consumed);
	
	public List<IFluidIngredient> getFluidIngredients();
	
	public List<IFluidIngredient> getFluidProducts();
	
}
