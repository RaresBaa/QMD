package lach_01298.qmd.nco.tile.fluid;

import lach_01298.qmd.nco.tile.ITileFiltered;
import lach_01298.qmd.nco.tile.internal.fluid.Tank;

import javax.annotation.Nonnull;
import java.util.List;

public interface ITileFilteredFluid extends ITileFiltered, ITileFluid {
	
	@Nonnull
	List<Tank> getTanksInternal();
	
	@Nonnull
	List<Tank> getFilterTanks();
	
	default void clearFilterTank(int tankNumber) {
		getFilterTanks().get(tankNumber).setFluidStored(null);
	}
}
