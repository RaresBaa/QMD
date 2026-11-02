package lach_01298.qmd.nco.legacy.container;

import lach_01298.qmd.nco.legacy.tile.*;

import javax.annotation.Nullable;

import lach_01298.qmd.nco.legacy.tile.ITileGuiLegacy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraftforge.fml.relauncher.*;

public class ContainerTileLegacy<T extends ITileGuiLegacy> extends NCContainerLegacy {
	
	protected final @Nullable IInventory inv;
	
	public ContainerTileLegacy(T tile) {
		super();
		inv = tile instanceof IInventory ? (IInventory) tile : null;
	}
	
	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		if (inv != null) {
			listener.sendAllWindowProperties(this, inv);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		if (inv != null) {
			inv.setField(id, data);
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return inv == null ? false : inv.isUsableByPlayer(player);
	}
}
