package lach_01298.qmd.nco.multiblock;

import lach_01298.qmd.nco.tile.multiblock.ITileLogicMultiblockPart;
import net.minecraft.world.World;

public interface IMultiblockLogic<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>> {
	
	String getID();
	
	World getWorld();
}
