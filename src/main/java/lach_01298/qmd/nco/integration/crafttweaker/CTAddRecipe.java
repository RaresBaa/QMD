package lach_01298.qmd.nco.integration.crafttweaker;

import crafttweaker.*;
import crafttweaker.api.item.IIngredient;
import lach_01298.qmd.nco.recipe.*;
import lach_01298.qmd.nco.recipe.ingredient.*;

import java.util.*;
import java.util.function.Function;

public class CTAddRecipe implements IAction {
	
	protected static boolean errored = false;
	
	protected final BasicRecipeHandler recipeHandler;
	protected BasicRecipe recipe;
	
	protected final List<IItemIngredient> itemIngredients = new ArrayList<>();
	protected final List<IFluidIngredient> fluidIngredients = new ArrayList<>();
	protected final List<IItemIngredient> itemProducts = new ArrayList<>();
	protected final List<IFluidIngredient> fluidProducts = new ArrayList<>();
	protected final List<Object> extras = new ArrayList<>();
	
	protected boolean nullInputs = true, nullIngredient, nullRecipe, wrongSize;
	
	public CTAddRecipe(BasicRecipeHandler recipeHandler, List<Object> objects) {
		this.recipeHandler = recipeHandler;
		
		for (int i = 0, len = objects.size(); i < len; ++i) {
			Object object = objects.get(i);
			if (i < recipeHandler.itemInputLastIndex) {
				if (!addIngredient(itemIngredients, object, CTHelper::buildAdditionItemIngredient)) {
					return;
				}
			}
			else if (i < recipeHandler.fluidInputLastIndex) {
				if (!addIngredient(fluidIngredients, object, CTHelper::buildAdditionFluidIngredient)) {
					return;
				}
			}
			else if (i < recipeHandler.itemOutputLastIndex) {
				if (!addIngredient(itemProducts, object, CTHelper::buildAdditionItemIngredient)) {
					return;
				}
			}
			else if (i < recipeHandler.fluidOutputLastIndex) {
				if (!addIngredient(fluidProducts, object, CTHelper::buildAdditionFluidIngredient)) {
					return;
				}
			}
			else {
				extras.add(object);
			}
		}
		
		if (itemIngredients.size() != recipeHandler.itemInputSize || fluidIngredients.size() != recipeHandler.fluidInputSize || itemProducts.size() != recipeHandler.itemOutputSize || fluidProducts.size() != recipeHandler.fluidOutputSize) {
			CraftTweakerAPI.logError("A " + recipeHandler.getName() + " recipe addition had the wrong size: " + RecipeHelper.getRecipeString(itemIngredients, fluidIngredients, itemProducts, fluidProducts));
			wrongSize = true;
			return;
		}
		
		recipe = recipeHandler.buildRecipe(itemIngredients, fluidIngredients, itemProducts, fluidProducts, extras, recipeHandler.isShapeless);
		if (recipe == null) {
			nullRecipe = true;
		}
	}
	
	protected <T, V extends lach_01298.qmd.nco.recipe.ingredient.IIngredient<T>> boolean addIngredient(List<V> ingredientList, Object object, Function<IIngredient, V> function) {
		if (object != null) {
			if (!(object instanceof IIngredient)) {
				nullIngredient = true;
				return false;
			}
			nullInputs = false;
		}
		V ingredient = function.apply((IIngredient) object);
		if (ingredient == null) {
			nullIngredient = true;
			return false;
		}
		else {
			ingredientList.add(ingredient);
			return true;
		}
	}
	
	@Override
	public void apply() {
		if (!isError()) {
			recipeHandler.addRecipe(recipe);
		}
	}
	
	@Override
	public String describe() {
		String recipeString = RecipeHelper.getRecipeString(itemIngredients, fluidIngredients, itemProducts, fluidProducts);
		if (!isError()) {
			return "Adding " + recipeHandler.getName() + " recipe: " + recipeString;
		}
		else {
			callError();
			String out = "Failed to add " + recipeHandler.getName() + " recipe " + recipeString;
			if (nullInputs) {
				return out + " as all ingredients were null";
			}
			if (nullIngredient) {
				return out + " as one or more ingredients had no match";
			}
			else {
				return out;
			}
		}
	}
	
	protected boolean isError() {
		return nullInputs || nullIngredient || nullRecipe || wrongSize;
	}
	
	protected static void callError() {
		if (!errored) {
			errored = true;
			CraftTweakerAPI.logError("At least one NuclearCraft recipe addition method has errored - check the log for more details");
		}
	}
}
