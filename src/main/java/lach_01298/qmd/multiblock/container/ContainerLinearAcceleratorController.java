package lach_01298.qmd.multiblock.container;

import lach_01298.qmd.accelerator.Accelerator;
import lach_01298.qmd.accelerator.tile.*;
import lach_01298.qmd.multiblock.network.AcceleratorUpdatePacket;
import lach_01298.qmd.nco.container.multiblock.controller.ContainerMultiblockController;
import lach_01298.qmd.nco.tile.TileContainerInfo;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerLinearAcceleratorController extends ContainerMultiblockController<Accelerator, IAcceleratorPart, AcceleratorUpdatePacket, TileLinearAcceleratorController, TileContainerInfo<TileLinearAcceleratorController>>
{

	public ContainerLinearAcceleratorController(EntityPlayer player,TileLinearAcceleratorController controller)
	{
		super(player, controller);
	}
}
