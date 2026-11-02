package lach_01298.qmd.pipe;

import lach_01298.qmd.nco.tile.TileContainerInfo;
import lach_01298.qmd.nco.tile.multiblock.ILogicMultiblockController;
import net.minecraft.tileentity.TileEntity;

public interface IPipeController<CONTROLLER extends TileEntity & IPipeController<CONTROLLER>> extends IPipePart, ILogicMultiblockController<Pipe, IPipePart, PipeUpdatePacket, CONTROLLER, TileContainerInfo<CONTROLLER>>
{

}
