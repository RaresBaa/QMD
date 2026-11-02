package lach_01298.qmd.nco.tile.generator;

import lach_01298.qmd.nco.tile.dummy.IInterfaceable;
import lach_01298.qmd.nco.tile.energy.*;
import lach_01298.qmd.nco.tile.internal.energy.EnergyConnection;
import lach_01298.qmd.nco.util.EnergyHelper;
import net.minecraft.util.ITickable;

public abstract class TilePassiveGenerator extends TileEnergy implements ITickable, IInterfaceable {
	
	public final int power;
	
	public TilePassiveGenerator(int maxPowerGen) {
		super(4L * maxPowerGen, 4 * maxPowerGen, ITileEnergy.energyConnectionAll(EnergyConnection.OUT));
		power = maxPowerGen;
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			getEnergyStorage().changeEnergyStored(getGenerated());
			pushEnergy();
		}
	}
	
	public abstract long getGenerated();
	
	@Override
	public int getSinkTier() {
		return 10;
	}
	
	@Override
	public int getSourceTier() {
		return EnergyHelper.getEUTier(power);
	}
}
