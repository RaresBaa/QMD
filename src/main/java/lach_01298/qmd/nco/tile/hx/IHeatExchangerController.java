package lach_01298.qmd.nco.tile.hx;

import lach_01298.qmd.nco.multiblock.hx.HeatExchanger;
import lach_01298.qmd.nco.network.multiblock.HeatExchangerUpdatePacket;
import lach_01298.qmd.nco.tile.TileContainerInfo;
import lach_01298.qmd.nco.tile.multiblock.ILogicMultiblockController;
import net.minecraft.tileentity.TileEntity;

public interface IHeatExchangerController<CONTROLLER extends TileEntity & IHeatExchangerController<CONTROLLER>> extends IHeatExchangerPart, ILogicMultiblockController<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, CONTROLLER, TileContainerInfo<CONTROLLER>> {
	
	boolean isRenderer();
	
	void setIsRenderer(boolean isRenderer);
}
