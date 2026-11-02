package lach_01298.qmd.nco.integration.crafttweaker;

import crafttweaker.*;
import crafttweaker.api.item.IIngredient;
import lach_01298.qmd.nco.recipe.*;
import lach_01298.qmd.nco.recipe.ingredient.*;

import java.util.*;

import static lach_01298.qmd.nco.recipe.IngredientSorption.INPUT;

public class CTRemoveRecipe implements IAction {
	
	protected static boolean errored = false;
	
	protected final BasicRecipeHandler recipeHandler;
	protected final IngredientSorption type;
	protected BasicRecipe recipe;
	
	protected final List<IItemIngredient> itemIngredients = new ArrayList<>();
	protected final List<IFluidIngredient> fluidIngredients = new ArrayList<>();
	
	protected boolean nullIngredient, nullRecipe, wrongSize;
	
	public CTRemoveRecipe(BasicRecipeHandler recipeHandler, IngredientSorption type, List<IIngredient> ctIngredients) {
		this.recipeHandler = recipeHandler;
		this.type = type;
		
		int itemSize = type == INPUT ? recipeHandler.itemInputSize : recipeHandler.itemOutputSize;
		int fluidSize = type == INPUT ? recipeHandler.fluidInputSize : recipeHandler.fluidOutputSize;
		
		for (int i = 0, len = ctIngredients.size(); i < len; ++i) {
			IIngredient ctIngredient = ctIngredients.get(i);
			if (i < itemSize) {
				if (!addIngredient(itemIngredients, CTHelper.buildRemovalItemIngredient(ctIngredient))) {
					return;
				}
			}
			else if (i < itemSize + fluidSize) {
				if (!addIngredient(fluidIngredients, CTHelper.buildRemovalFluidIngredient(ctIngredient))) {
					return;
				}
			}
		}
		
		if (itemIngredients.size() != itemSize || fluidIngredients.size() != fluidSize) {
			CraftTweakerAPI.logError("A " + recipeHandler.getName() + " recipe removal had the wrong number of " + (type == INPUT ? "inputs" : "outputs") + ": " + RecipeHelper.getAllIngredientNamesConcat(itemIngredients, fluidIngredients));
			wrongSize = true;
			return;
		}
		
		recipe = getRecipe();
		if (recipe == null) {
			nullRecipe = true;
		}
	}
	
	protected <T> boolean addIngredient(List<T> ingredientList, T ingredient) {
		if (ingredient == null) {
			nullIngredient = true;
			return false;
		}
		else {
			ingredientList.add(ingredient);
			return true;
		}
	}
	
	protected BasicRecipe getRecipe() {
		return type == INPUT ? recipeHandler.getRecipeFromIngredients(itemIngredients, fluidIngredients) : recipeHandler.getRecipeFromProducts(itemIngredients, fluidIngredients);
	}
	
	@Override
	public void apply() {
		if (!isError()) {
			while (recipeHandler.removeRecipe(recipe)) {
				recipe = getRecipe();
			}
		}
	}
	
	@Override
	public String describe() {
		if (!isError()) {
			if (type == INPUT) {
				return "Removing " + recipeHandler.getName() + " recipe: " + RecipeHelper.getRecipeString(recipe);
			}
			else {
				return "Removing " + recipeHandler.getName() + " recipes for: " + RecipeHelper.getAllIngredientNamesConcat(itemIngredients, fluidIngredients);
			}
		}
		else {
			callError();
			String out = "Failed to remove " + recipeHandler.getName() + " recipe with " + RecipeHelper.getAllIngredientNamesConcat(itemIngredients, fluidIngredients) + " as the " + (type == INPUT ? "input" : "output");
			if (nullIngredient) {
				return out + " as one or more " + (type == INPUT ? "ingredients" : "products") + " had no match";
			}
			else if (nullRecipe) {
				return out + " as no matching recipe could be found";
			}
			else {
				return out;
			}
		}
	}
	
	protected boolean isError() {
		return nullIngredient || nullRecipe || wrongSize;
	}
	
	protected static void callError() {
		if (!errored) {
			errored = true;
			CraftTweakerAPI.logError("At least one NuclearCraft recipe removal method has errored - check the log for more details");
		}
	}
}
