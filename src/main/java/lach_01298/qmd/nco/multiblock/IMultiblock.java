package lach_01298.qmd.nco.multiblock;

import lach_01298.qmd.nco.tile.multiblock.ITileMultiblockPart;
import net.minecraft.world.World;

public interface IMultiblock<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>> {
	
	World getWorld();
}
