package lach_01298.qmd.nco.multiblock;

import lach_01298.qmd.nco.network.multiblock.MultiblockUpdatePacket;
import lach_01298.qmd.nco.tile.multiblock.ITileMultiblockPart;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

public interface IPacketMultiblock<MULTIBLOCK extends Multiblock<MULTIBLOCK, T>, T extends ITileMultiblockPart<MULTIBLOCK, T>, PACKET extends MultiblockUpdatePacket> extends IMultiblock<MULTIBLOCK, T> {
	
	Set<EntityPlayer> getMultiblockUpdatePacketListeners();
	
	PACKET getMultiblockUpdatePacket();
	
	void onMultiblockUpdatePacket(PACKET message);
	
	default void addMultiblockUpdatePacketListener(EntityPlayer playerToUpdate) {
		getMultiblockUpdatePacketListeners().add(playerToUpdate);
		sendMultiblockUpdatePacketToPlayer(playerToUpdate);
	}
	
	default void removeMultiblockUpdatePacketListener(EntityPlayer playerToRemove) {
		getMultiblockUpdatePacketListeners().remove(playerToRemove);
	}
	
	default void sendMultiblockUpdatePacketToListeners() {
		if (getWorld().isRemote) {
			return;
		}
		PACKET packet = getMultiblockUpdatePacket();
		if (packet == null) {
			return;
		}
		for (EntityPlayer player : getMultiblockUpdatePacketListeners()) {
			packet.sendTo(player);
		}
	}
	
	default void sendMultiblockUpdatePacketToPlayer(EntityPlayer player) {
		if (getWorld().isRemote) {
			return;
		}
		PACKET packet = getMultiblockUpdatePacket();
		if (packet == null) {
			return;
		}
		packet.sendTo(player);
	}
	
	default void sendMultiblockUpdatePacketToAll() {
		if (getWorld().isRemote) {
			return;
		}
		PACKET packet = getMultiblockUpdatePacket();
		if (packet == null) {
			return;
		}
		packet.sendToAll();
	}
}
