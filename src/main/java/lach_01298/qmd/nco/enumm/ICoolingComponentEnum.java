package lach_01298.qmd.nco.enumm;

import net.minecraft.tileentity.TileEntity;

public interface ICoolingComponentEnum<T extends TileEntity> extends ITileEnum<T> {
	
	int getCooling();
}
