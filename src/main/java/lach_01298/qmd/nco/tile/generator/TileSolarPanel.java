package lach_01298.qmd.nco.tile.generator;

import net.minecraft.util.EnumFacing;

import static nc.config.NCConfig.solar_power;

public class TileSolarPanel extends TilePassiveGenerator {
	
	public static class Basic extends TileSolarPanel {
		
		public Basic() {
			super(solar_power[0]);
		}
	}
	
	public static class Advanced extends TileSolarPanel {
		
		public Advanced() {
			super(solar_power[1]);
		}
	}
	
	public static class DU extends TileSolarPanel {
		
		public DU() {
			super(solar_power[2]);
		}
	}
	
	public static class Elite extends TileSolarPanel {
		
		public Elite() {
			super(solar_power[3]);
		}
	}
	
	public TileSolarPanel(int power) {
		super(power);
	}
	
	@Override
	public long getGenerated() {
		if (world.canBlockSeeSky(pos.offset(EnumFacing.UP))) {
			return (long) (world.getSunBrightnessFactor(1F) * power);
		}
		return 0;
	}
}
