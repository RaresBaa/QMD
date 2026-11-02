package lach_01298.qmd.nco.recipe.ingredient;

public interface IChanceItemIngredient extends IItemIngredient {
	
	IItemIngredient getRawIngredient();
	
	int getChancePercent();
	
	int getMinStackSize();
	
	double getMeanStackSize();
}
