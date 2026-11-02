package lach_01298.qmd.nco.gui;

import lach_01298.qmd.nco.container.ContainerFunction;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface GuiFunction<TILE extends TileEntity> {
	
	GuiContainer apply(EntityPlayer player, TILE tile);
	
	static <T extends TileEntity> GuiFunction<T> of(String modId, String name, ContainerFunction<T> containerFunction, GuiInfoTileFunction<T> guiFunction) {
		if (containerFunction == null || guiFunction == null) {
			return null;
		}
		else {
			return (player, tile) -> guiFunction.apply(containerFunction.apply(player, tile), player, tile, modId + ":textures/gui/container/" + name + ".png");
		}
	}
}
