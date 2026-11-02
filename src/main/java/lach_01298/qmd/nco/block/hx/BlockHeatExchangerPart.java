package lach_01298.qmd.nco.block.hx;

import lach_01298.qmd.nco.block.multiblock.BlockMultiblockPart;
import lach_01298.qmd.tab.QMDTabs;
import net.minecraft.block.material.Material;

public abstract class BlockHeatExchangerPart extends BlockMultiblockPart {
	
	public BlockHeatExchangerPart() {
		super(Material.IRON, QMDTabs.MULTIBLOCKS);
	}
	
	public static abstract class Transparent extends BlockMultiblockPart.Transparent {
		
		public Transparent(boolean smartRender) {
			super(Material.IRON, QMDTabs.MULTIBLOCKS, smartRender);
		}
	}
}
