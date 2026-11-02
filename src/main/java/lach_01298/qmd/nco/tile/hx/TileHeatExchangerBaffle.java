package lach_01298.qmd.nco.tile.hx;

import lach_01298.qmd.nco.multiblock.cuboidal.CuboidalPartPositionType;
import lach_01298.qmd.nco.multiblock.hx.HeatExchanger;

public class TileHeatExchangerBaffle extends TileHeatExchangerPart {
	
	public TileHeatExchangerBaffle() {
		super(CuboidalPartPositionType.INTERIOR);
	}
	
	@Override
	public void onMachineAssembled(HeatExchanger multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
	}
}
