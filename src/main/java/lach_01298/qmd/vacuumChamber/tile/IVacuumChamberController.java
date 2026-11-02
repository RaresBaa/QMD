package lach_01298.qmd.vacuumChamber.tile;

import lach_01298.qmd.multiblock.network.VacuumChamberUpdatePacket;
import lach_01298.qmd.vacuumChamber.VacuumChamber;
import lach_01298.qmd.nco.tile.TileContainerInfo;
import lach_01298.qmd.nco.tile.multiblock.ILogicMultiblockController;
import net.minecraft.tileentity.TileEntity;

public interface IVacuumChamberController<CONTROLLER extends TileEntity & IVacuumChamberController<CONTROLLER>> extends IVacuumChamberPart, ILogicMultiblockController<VacuumChamber, IVacuumChamberPart, VacuumChamberUpdatePacket, CONTROLLER, TileContainerInfo<CONTROLLER>>
{

}
