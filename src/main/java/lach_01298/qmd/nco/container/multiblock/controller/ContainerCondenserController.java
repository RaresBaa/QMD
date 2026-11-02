package lach_01298.qmd.nco.container.multiblock.controller;

import lach_01298.qmd.nco.multiblock.hx.HeatExchanger;
import lach_01298.qmd.nco.network.multiblock.HeatExchangerUpdatePacket;
import lach_01298.qmd.nco.tile.TileContainerInfo;
import lach_01298.qmd.nco.tile.hx.*;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCondenserController extends ContainerMultiblockController<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, TileCondenserController, TileContainerInfo<TileCondenserController>> {
	
	public ContainerCondenserController(EntityPlayer player, TileCondenserController controller) {
		super(player, controller);
	}
}
