package lach_01298.qmd.nco.legacy.tile;

import lach_01298.qmd.nco.tile.dummy.IInterfaceable;
import lach_01298.qmd.nco.util.NCMath;
import net.minecraft.util.ITickable;

public abstract interface IProcessorLegacy extends ITickable, IInterfaceable {
	
	public void refreshRecipe();
	
	public void refreshActivity();
	
	public void refreshActivityOnProduction();

}
