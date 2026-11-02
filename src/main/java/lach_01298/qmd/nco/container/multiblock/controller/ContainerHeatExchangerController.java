package lach_01298.qmd.nco.container.multiblock.controller;

import lach_01298.qmd.nco.multiblock.hx.HeatExchanger;
import lach_01298.qmd.nco.network.multiblock.HeatExchangerUpdatePacket;
import lach_01298.qmd.nco.tile.TileContainerInfo;
import lach_01298.qmd.nco.tile.hx.*;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerHeatExchangerController extends ContainerMultiblockController<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, TileHeatExchangerController, TileContainerInfo<TileHeatExchangerController>> {
	
	public ContainerHeatExchangerController(EntityPlayer player, TileHeatExchangerController controller) {
		super(player, controller);
	}
}
