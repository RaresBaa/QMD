package lach_01298.qmd.nco.tile.hx;

import lach_01298.qmd.nco.multiblock.cuboidal.CuboidalPartPositionType;
import lach_01298.qmd.nco.multiblock.hx.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileHeatExchangerRedstonePort extends TileHeatExchangerPart {
	
	public TileHeatExchangerRedstonePort() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(HeatExchanger multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
	}
	
	@Override
	public void onBlockNeighborChanged(IBlockState state, World worldIn, BlockPos posIn, BlockPos fromPos) {
		super.onBlockNeighborChanged(state, worldIn, posIn, fromPos);
		setActivity(getIsRedstonePowered());
		
		HeatExchangerLogic logic = getLogic();
		if (logic != null) {
			logic.setIsExchangerOn();
		}
	}
}
