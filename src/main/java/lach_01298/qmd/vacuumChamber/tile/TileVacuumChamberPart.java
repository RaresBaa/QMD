package lach_01298.qmd.vacuumChamber.tile;

import lach_01298.qmd.vacuumChamber.VacuumChamber;
import lach_01298.qmd.nco.multiblock.cuboidal.*;

public abstract class TileVacuumChamberPart extends TileCuboidalMultiblockPart<VacuumChamber, IVacuumChamberPart> implements IVacuumChamberPart
{


	public TileVacuumChamberPart(CuboidalPartPositionType positionType)
	{
		super(VacuumChamber.class, IVacuumChamberPart.class, positionType);
	}

	@Override
	public VacuumChamber createNewMultiblock()
	{
		return new VacuumChamber(world);
	}



}
