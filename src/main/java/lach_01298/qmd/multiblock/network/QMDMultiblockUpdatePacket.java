package lach_01298.qmd.multiblock.network;

import lach_01298.qmd.network.QMDPackets;
import lach_01298.qmd.nco.network.multiblock.MultiblockUpdatePacket;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public abstract class QMDMultiblockUpdatePacket extends MultiblockUpdatePacket {
	
	public QMDMultiblockUpdatePacket() {
		super();
	}
	
	public SimpleNetworkWrapper getWrapper() {
		return QMDPackets.wrapper;
	}
}
