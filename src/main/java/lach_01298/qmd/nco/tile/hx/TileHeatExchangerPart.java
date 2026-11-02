package lach_01298.qmd.nco.tile.hx;

import lach_01298.qmd.nco.multiblock.cuboidal.*;
import lach_01298.qmd.nco.multiblock.hx.HeatExchanger;

public abstract class TileHeatExchangerPart extends TileCuboidalMultiblockPart<HeatExchanger, IHeatExchangerPart> implements IHeatExchangerPart {
	
	public TileHeatExchangerPart(CuboidalPartPositionType positionType) {
		super(HeatExchanger.class, IHeatExchangerPart.class, positionType);
	}
	
	@Override
	public HeatExchanger createNewMultiblock() {
		return new HeatExchanger(world);
	}
}
