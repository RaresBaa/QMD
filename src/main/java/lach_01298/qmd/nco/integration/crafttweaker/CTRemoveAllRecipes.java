package lach_01298.qmd.nco.integration.crafttweaker;

import crafttweaker.IAction;
import lach_01298.qmd.nco.recipe.BasicRecipeHandler;

public class CTRemoveAllRecipes implements IAction {
	
	protected final BasicRecipeHandler recipeHandler;
	
	public CTRemoveAllRecipes(BasicRecipeHandler recipeHandler) {
		this.recipeHandler = recipeHandler;
	}
	
	@Override
	public void apply() {
		recipeHandler.removeAllRecipes();
	}
	
	@Override
	public String describe() {
		return String.format("Removed all %s recipes", recipeHandler.getName());
	}
}
