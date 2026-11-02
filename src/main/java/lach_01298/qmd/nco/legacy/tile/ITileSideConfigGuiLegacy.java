package lach_01298.qmd.nco.legacy.tile;

import lach_01298.qmd.nco.network.tile.TileUpdatePacket;

public interface ITileSideConfigGuiLegacy<PACKET extends TileUpdatePacket> extends ITileGuiLegacy<PACKET> {
	
	int getSideConfigXOffset();
	
	int getSideConfigYOffset();
}
