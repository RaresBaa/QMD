package lach_01298.qmd.nco.gui.multiblock.controller;

import lach_01298.qmd.nco.multiblock.*;
import lach_01298.qmd.nco.network.multiblock.MultiblockUpdatePacket;
import lach_01298.qmd.nco.tile.TileContainerInfo;
import lach_01298.qmd.nco.tile.multiblock.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public abstract class GuiLogicMultiblockController<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T> & IPacketMultiblock<MULTIBLOCK, T, PACKET>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, PACKET extends MultiblockUpdatePacket, CONTROLLER extends TileEntity & IMultiblockGuiPart<MULTIBLOCK, T, PACKET, CONTROLLER, INFO>, INFO extends TileContainerInfo<CONTROLLER>, L extends LOGIC> extends GuiMultiblockController<MULTIBLOCK, T, PACKET, CONTROLLER, INFO> {
	
	protected final LOGIC logic;
	
	public GuiLogicMultiblockController(Container inventory, EntityPlayer player, CONTROLLER controller, String textureLocation) {
		super(inventory, player, controller, textureLocation);
		
		logic = multiblock.getLogic();
	}
	
	@SuppressWarnings("unchecked")
	protected L getLogic() {
		return (L) logic;
	}
}
