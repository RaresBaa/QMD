package lach_01298.qmd.nco.block.tile.info;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;

import java.util.function.Supplier;

public class BlockSimpleTileInfo<TILE extends TileEntity> extends BlockTileInfo<TILE> {
	
	public BlockSimpleTileInfo(String modId, String name, Class<TILE> tileClass, Supplier<TILE> tileSupplier, CreativeTabs creativeTab) {
		super(modId, name, tileClass, tileSupplier, creativeTab);
	}
}
