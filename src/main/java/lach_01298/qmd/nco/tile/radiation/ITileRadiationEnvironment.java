package lach_01298.qmd.nco.tile.radiation;

import nc.radiation.environment.RadiationEnvironmentInfo;
import lach_01298.qmd.nco.tile.ITile;
import lach_01298.qmd.nco.util.FourPos;

public interface ITileRadiationEnvironment extends ITile {
	
	default FourPos getFourPos() {
		return new FourPos(getTilePos(), getTileWorld().provider.getDimension());
	}
	
	void checkRadiationEnvironmentInfo();
	
	void handleRadiationEnvironmentInfo(RadiationEnvironmentInfo info);
	
	double getRadiationContributionFraction();
	
	double getCurrentChunkRadiationLevel();
	
	void setCurrentChunkRadiationLevel(double level);
	
	double getCurrentChunkRadiationBuffer();
	
	void setCurrentChunkRadiationBuffer(double buffer);
}
