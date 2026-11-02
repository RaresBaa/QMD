package lach_01298.qmd.nco.enumm;

import net.minecraft.tileentity.TileEntity;

public interface ITileEnum<T extends TileEntity> {
	
	Class<? extends T> getTileClass();
}
