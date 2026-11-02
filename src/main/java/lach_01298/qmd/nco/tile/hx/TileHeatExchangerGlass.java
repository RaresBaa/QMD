package lach_01298.qmd.nco.tile.hx;

import lach_01298.qmd.nco.multiblock.cuboidal.CuboidalPartPositionType;
import lach_01298.qmd.nco.multiblock.hx.HeatExchanger;

public class TileHeatExchangerGlass extends TileHeatExchangerPart {
	
	public TileHeatExchangerGlass() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(HeatExchanger multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
	}
}
