package lach_01298.qmd.nco.multiblock;

import lach_01298.qmd.nco.network.multiblock.MultiblockUpdatePacket;
import lach_01298.qmd.nco.tile.multiblock.ITileLogicMultiblockPart;

public interface IPacketMultiblockLogic<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, PACKET extends MultiblockUpdatePacket> extends IMultiblockLogic<MULTIBLOCK, LOGIC, T> {
	
	PACKET getMultiblockUpdatePacket();
	
	void onMultiblockUpdatePacket(PACKET message);
}
