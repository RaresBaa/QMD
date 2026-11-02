package lach_01298.qmd.nco.tile.multiblock;

import lach_01298.qmd.nco.multiblock.*;
import lach_01298.qmd.nco.network.multiblock.MultiblockUpdatePacket;
import lach_01298.qmd.nco.tile.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import java.util.Set;

public interface IMultiblockGuiPart<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & IPacketMultiblock<MULTIBLOCK, T, PACKET>, T extends ITileMultiblockPart<MULTIBLOCK, T>, PACKET extends MultiblockUpdatePacket, GUITILE extends TileEntity & IMultiblockGuiPart<MULTIBLOCK, T, PACKET, GUITILE, INFO>, INFO extends TileContainerInfo<GUITILE>> extends ITileMultiblockPart<MULTIBLOCK, T>, ITileGui<GUITILE, PACKET, INFO> {
	
	default void sendTileUpdatePacketToPlayer(EntityPlayer player) {
		if (!getTileWorld().isRemote && getMultiblock() != null) {
			getTileUpdatePacket().sendTo(player);
		}
	}
	
	default void sendTileUpdatePacketToAll() {
		if (getMultiblock() != null) {
			getTileUpdatePacket().sendToAll();
		}
	}
	
	default Set<EntityPlayer> getTileUpdatePacketListeners() {
		MULTIBLOCK multiblock = getMultiblock();
		return multiblock == null ? null : multiblock.getMultiblockUpdatePacketListeners();
	}
	
	default void addTileUpdatePacketListener(EntityPlayer player) {
		if (getMultiblock() != null) {
			getTileUpdatePacketListeners().add(player);
			sendTileUpdatePacketToPlayer(player);
		}
	}
	
	default void removeTileUpdatePacketListener(EntityPlayer player) {
		if (getMultiblock() != null) {
			getTileUpdatePacketListeners().remove(player);
		}
	}
	
	default void sendTileUpdatePacketToListeners() {
		if (getMultiblock() != null) {
			getTileUpdatePacket().sendTo(getTileUpdatePacketListeners());
		}
	}
}
