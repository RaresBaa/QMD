package lach_01298.qmd.nco.tile.processor;

import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * What QMD uses of NCO's processor interface. QMD's own machines run on the legacy classes.
 */
public interface IProcessor {
	
	void refreshRecipe();
	
	void refreshActivity();
	
	class HandlerPair {
		
		public final IItemHandler itemHandler;
		public final IFluidHandler fluidHandler;
		
		public HandlerPair(IItemHandler itemHandler, IFluidHandler fluidHandler) {
			this.itemHandler = itemHandler;
			this.fluidHandler = fluidHandler;
		}
	}
}
