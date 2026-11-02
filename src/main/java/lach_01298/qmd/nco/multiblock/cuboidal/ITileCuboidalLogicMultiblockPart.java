package lach_01298.qmd.nco.multiblock.cuboidal;

import lach_01298.qmd.nco.multiblock.*;
import lach_01298.qmd.nco.tile.multiblock.ITileLogicMultiblockPart;

public interface ITileCuboidalLogicMultiblockPart<MULTIBLOCK extends CuboidalMultiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileCuboidalLogicMultiblockPart<MULTIBLOCK, LOGIC, T>> extends ITileCuboidalMultiblockPart<MULTIBLOCK, T>, ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T> {
	
}
