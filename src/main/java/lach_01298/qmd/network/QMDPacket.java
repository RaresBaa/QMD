package lach_01298.qmd.network;

import lach_01298.qmd.nco.network.NCPacket;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public abstract class QMDPacket extends NCPacket {
	
	public QMDPacket() {
		super();
	}
	
	public SimpleNetworkWrapper getWrapper() {
		return QMDPackets.wrapper;
	}
}
