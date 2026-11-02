package lach_01298.qmd.multiblock.container;

import lach_01298.qmd.accelerator.Accelerator;
import lach_01298.qmd.accelerator.tile.*;
import lach_01298.qmd.multiblock.network.AcceleratorUpdatePacket;
import lach_01298.qmd.nco.container.multiblock.controller.ContainerMultiblockController;
import lach_01298.qmd.nco.tile.TileContainerInfo;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerBeamDiverterController extends ContainerMultiblockController<Accelerator, IAcceleratorPart, AcceleratorUpdatePacket, TileBeamDiverterController, TileContainerInfo<TileBeamDiverterController>>
{

	public ContainerBeamDiverterController(EntityPlayer player,TileBeamDiverterController controller)
	{
		super(player, controller);
	}
}
