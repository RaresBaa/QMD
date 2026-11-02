package lach_01298.qmd.nco.tile.passive;

import lach_01298.qmd.nco.tile.energy.ITileEnergy;
import lach_01298.qmd.nco.tile.fluid.ITileFluid;
import lach_01298.qmd.nco.tile.inventory.ITileInventory;
import net.minecraft.util.ITickable;

public interface ITilePassive extends ITickable, ITileEnergy, ITileInventory, ITileFluid {
	
	double getEnergyRate();
	
	double getItemRate();
	
	double getFluidRate();
	
	boolean canPushEnergyTo();
	
	boolean canPushItemsTo();
	
	boolean canPushFluidsTo();
}
