package lach_01298.qmd.nco.legacy.tile;

import lach_01298.qmd.nco.network.tile.TileUpdatePacket;
import lach_01298.qmd.nco.tile.ITilePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Set;

public interface ITileGuiLegacy<PACKET extends TileUpdatePacket> extends ITilePacket<PACKET> {
	
	int getGuiID();
	
	Set<EntityPlayer> getTileUpdatePacketListeners();
	
	default void addTileUpdatePacketListener(EntityPlayer player) {
		getTileUpdatePacketListeners().add(player);
		sendTileUpdatePacketToPlayer(player);
	}
	
	default void removeTileUpdatePacketListener(EntityPlayer player) {
		getTileUpdatePacketListeners().remove(player);
	}
	
	default void sendTileUpdatePacketToListeners() {
		for (EntityPlayer player : getTileUpdatePacketListeners()) {
			getTileUpdatePacket().sendTo(player);
		}
	}
}
