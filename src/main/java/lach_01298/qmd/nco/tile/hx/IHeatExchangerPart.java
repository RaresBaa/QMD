package lach_01298.qmd.nco.tile.hx;

import lach_01298.qmd.nco.multiblock.cuboidal.ITileCuboidalLogicMultiblockPart;
import lach_01298.qmd.nco.multiblock.hx.*;

public interface IHeatExchangerPart extends ITileCuboidalLogicMultiblockPart<HeatExchanger, HeatExchangerLogic, IHeatExchangerPart> {
	
	default void refreshHeatExchangerRecipe() {
		HeatExchangerLogic logic = getLogic();
		if (logic != null) {
			logic.refreshRecipe();
		}
	}
	
	default void refreshHeatExchangerActivity() {
		HeatExchangerLogic logic = getLogic();
		if (logic != null) {
			logic.refreshActivity();
		}
	}
}
