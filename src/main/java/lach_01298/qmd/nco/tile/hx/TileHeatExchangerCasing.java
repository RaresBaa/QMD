package lach_01298.qmd.nco.tile.hx;

import lach_01298.qmd.nco.block.property.BlockProperties;
import lach_01298.qmd.nco.multiblock.cuboidal.CuboidalPartPositionType;
import lach_01298.qmd.nco.multiblock.hx.HeatExchanger;

public class TileHeatExchangerCasing extends TileHeatExchangerPart {
	
	public TileHeatExchangerCasing() {
		super(CuboidalPartPositionType.EXTERIOR);
	}
	
	@Override
	public void onMachineAssembled(HeatExchanger multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
		if (!world.isRemote && getPartPosition().isFrame()) {
			world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockProperties.FRAME, true), 2);
		}
	}
	
	@Override
	public void onMachineBroken() {
		if (!world.isRemote && getPartPosition().isFrame()) {
			world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockProperties.FRAME, false), 2);
		}
		super.onMachineBroken();
	}
}
