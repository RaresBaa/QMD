package lach_01298.qmd.nco.container;

import lach_01298.qmd.nco.network.NCPacket;
import lach_01298.qmd.nco.tile.*;
import net.minecraft.tileentity.TileEntity;

public abstract class ContainerInfoTile<TILE extends TileEntity & ITileGui<TILE, PACKET, INFO>, PACKET extends NCPacket, INFO extends TileContainerInfo<TILE>> extends NCContainer {
	
	protected final INFO info;
	
	protected ContainerInfoTile(TILE tile) {
		super(tile);
		info = tile.getContainerInfo();
	}
}
