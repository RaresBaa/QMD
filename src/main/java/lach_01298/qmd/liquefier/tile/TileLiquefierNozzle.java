package lach_01298.qmd.liquefier.tile;

import lach_01298.qmd.nco.multiblock.cuboidal.CuboidalPartPositionType;
import lach_01298.qmd.nco.multiblock.hx.HeatExchanger;
import lach_01298.qmd.nco.multiblock.hx.HeatExchangerTubeNetwork;
import lach_01298.qmd.nco.tile.hx.TileHeatExchangerPart;

import javax.annotation.Nullable;

public class TileLiquefierNozzle extends TileHeatExchangerPart
{
	public @Nullable HeatExchangerTubeNetwork network;

	public TileLiquefierNozzle()
	{
		super(CuboidalPartPositionType.INTERIOR);
	}

	@Override
	public void onMachineAssembled(HeatExchanger multiblock)
	{
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
	}


}