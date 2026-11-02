package lach_01298.qmd.nco.legacy.jei;

import lach_01298.qmd.nco.legacy.tile.*;

import lach_01298.qmd.nco.recipe.BasicRecipeHandler;
import mezz.jei.api.IGuiHelper;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IJEIHandlerLegacy<WRAPPER extends JEIBasicRecipeWrapperLegacy<WRAPPER>> {
	
	JEIBasicCategoryLegacy<?> getCategory(IGuiHelper guiHelper);
	
	BasicRecipeHandler getRecipeHandler();
	
	Class<WRAPPER> getRecipeWrapperClass();
	
	List<WRAPPER> getJEIRecipes(IGuiHelper guiHelper);
	
	String getUid();
	
	boolean getEnabled();
	
	List<ItemStack> getCrafters();
	
	String getTextureName();
}
