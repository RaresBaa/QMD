package lach_01298.qmd.multiblock.container;

import lach_01298.qmd.liquefier.tile.TileLiquefierController;
import lach_01298.qmd.nco.container.multiblock.controller.ContainerMultiblockController;
import lach_01298.qmd.nco.multiblock.hx.HeatExchanger;
import lach_01298.qmd.nco.network.multiblock.HeatExchangerUpdatePacket;
import lach_01298.qmd.nco.tile.TileContainerInfo;
import lach_01298.qmd.nco.tile.hx.IHeatExchangerPart;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerLiquefierController extends ContainerMultiblockController<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, TileLiquefierController, TileContainerInfo<TileLiquefierController>>
{
	public ContainerLiquefierController(EntityPlayer player, TileLiquefierController controller)
	{
		super(player, controller);
	}
}
