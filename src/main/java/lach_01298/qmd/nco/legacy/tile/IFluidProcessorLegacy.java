package lach_01298.qmd.nco.legacy.tile;

import java.util.*;

import it.unimi.dsi.fastutil.ints.*;
import lach_01298.qmd.nco.recipe.ingredient.IFluidIngredient;
import lach_01298.qmd.nco.tile.fluid.ITileFluid;
import lach_01298.qmd.nco.tile.internal.fluid.*;

public interface IFluidProcessorLegacy extends IProcessorLegacy, ITileFluid {
	
	public int getFluidInputSize();
	
	public int getFluidOutputputSize();
	
	public List<Tank> getFluidInputs();
	
	public List<IFluidIngredient> getFluidIngredients();
	
	public List<IFluidIngredient> getFluidProducts();
	
	public static IntList defaultTankCapacities(int capacity, int inSize, int outSize) {
		IntList tankCapacities = new IntArrayList();
		for (int i = 0; i < inSize + outSize; i++) {
			tankCapacities.add(capacity);
		}
		return tankCapacities;
	}
	
	public static List<TankSorption> defaultTankSorptions(int inSize, int outSize) {
		List<TankSorption> tankSorptions = new ArrayList<>();
		for (int i = 0; i < inSize; i++) {
			tankSorptions.add(TankSorption.IN);
		}
		for (int i = 0; i < outSize; i++) {
			tankSorptions.add(TankSorption.OUT);
		}
		return tankSorptions;
	}
}
