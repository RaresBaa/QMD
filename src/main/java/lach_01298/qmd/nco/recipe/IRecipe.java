package lach_01298.qmd.nco.recipe;

import crafttweaker.annotations.ZenRegister;
import lach_01298.qmd.nco.recipe.ingredient.*;
import lach_01298.qmd.nco.tile.internal.fluid.Tank;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import stanhebben.zenscript.annotations.*;

import java.util.List;

public interface IRecipe {
	
	List<IItemIngredient> getItemIngredients();
	
	List<IFluidIngredient> getFluidIngredients();
	
	List<IItemIngredient> getItemProducts();
	
	List<IFluidIngredient> getFluidProducts();
	
	@ZenMethod("getItemIngredient")
	@Optional.Method(modid = "crafttweaker")
	default crafttweaker.api.item.IIngredient ctItemIngredient(int index) {
		return getItemIngredients().get(index).ct();
	}
	
	@ZenMethod("getFluidIngredient")
	@Optional.Method(modid = "crafttweaker")
	default crafttweaker.api.item.IIngredient ctFluidIngredient(int index) {
		return getFluidIngredients().get(index).ct();
	}
	
	@ZenMethod("getItemProduct")
	@Optional.Method(modid = "crafttweaker")
	default crafttweaker.api.item.IIngredient ctItemProduct(int index) {
		return getItemProducts().get(index).ct();
	}
	
	@ZenMethod("getFluidProduct")
	@Optional.Method(modid = "crafttweaker")
	default crafttweaker.api.item.IIngredient ctFluidProduct(int index) {
		return getFluidProducts().get(index).ct();
	}
	
	@ZenMethod
	List<Object> getExtras();
	
	RecipeMatchResult matchInputs(List<ItemStack> itemInputs, List<Tank> fluidInputs);
	
	RecipeMatchResult matchOutputs(List<ItemStack> itemOutputs, List<Tank> fluidOutputs);
	
	RecipeMatchResult matchIngredients(List<IItemIngredient> itemIngredientsIn, List<IFluidIngredient> fluidIngredientsIn);
	
	RecipeMatchResult matchProducts(List<IItemIngredient> itemProductsIn, List<IFluidIngredient> fluidProductsIn);
}
