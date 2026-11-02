package lach_01298.qmd.nco.tile.multiblock;

import lach_01298.qmd.nco.multiblock.*;
import lach_01298.qmd.nco.network.multiblock.MultiblockUpdatePacket;
import lach_01298.qmd.nco.tile.TileContainerInfo;
import net.minecraft.tileentity.TileEntity;

public interface ILogicMultiblockController<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & IPacketMultiblock<MULTIBLOCK, T, PACKET>, T extends ITileMultiblockPart<MULTIBLOCK, T>, PACKET extends MultiblockUpdatePacket, CONTROLLER extends TileEntity & ILogicMultiblockController<MULTIBLOCK, T, PACKET, CONTROLLER, INFO>, INFO extends TileContainerInfo<CONTROLLER>> extends IMultiblockController<MULTIBLOCK, T, PACKET, CONTROLLER, INFO> {
	
	String getLogicID();
}
