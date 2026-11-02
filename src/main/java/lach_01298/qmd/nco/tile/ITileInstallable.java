package lach_01298.qmd.nco.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;

public interface ITileInstallable extends ITile {
	
	boolean tryInstall(EntityPlayer player, EnumHand hand, EnumFacing facing);
}
