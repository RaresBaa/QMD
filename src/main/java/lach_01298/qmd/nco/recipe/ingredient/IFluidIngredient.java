package lach_01298.qmd.nco.recipe.ingredient;

import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IFluidIngredient extends IIngredient<FluidStack> {
	
	@Override
	default FluidStack getNextStack(int ingredientNumber) {
		FluidStack nextStack = getStack();
		nextStack.amount = getNextStackSize(ingredientNumber);
		return nextStack;
	}
	
	@Override
	default List<FluidStack> getInputStackHashingList() {
		return getInputStackList();
	}
	
	@Override
	IFluidIngredient getFactoredIngredient(int factor);
}
