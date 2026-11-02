package lach_01298.qmd.nco.recipe.ingredient;

import it.unimi.dsi.fastutil.ints.IntList;
import lach_01298.qmd.nco.recipe.*;
import net.minecraftforge.fml.common.Optional;

import java.util.List;

public interface IIngredient<T> {
	
	default void init() {}
	
	T getStack();
	
	T getNextStack(int ingredientNumber);
	
	List<T> getInputStackList();
	
	List<T> getInputStackHashingList();
	
	List<T> getOutputStackList();
	
	int getMaxStackSize(int ingredientNumber);
	
	void setMaxStackSize(int stackSize);
	
	default int getNextStackSize(int ingredientNumber) {
		return getMaxStackSize(ingredientNumber);
	}
	
	String getIngredientName();
	
	String getIngredientNamesConcat();
	
	IntList getFactors();
	
	IIngredient<T> getFactoredIngredient(int factor);
	
	IngredientMatchResult match(Object object, IngredientSorption sorption);
	
	boolean isValid();
	
	boolean isEmpty();
	
	// CraftTweaker
	
	@Optional.Method(modid = "crafttweaker")
	crafttweaker.api.item.IIngredient ct();
}
