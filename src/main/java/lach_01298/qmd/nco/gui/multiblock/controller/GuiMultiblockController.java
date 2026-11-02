package lach_01298.qmd.nco.gui.multiblock.controller;

import com.google.common.collect.Lists;
import lach_01298.qmd.nco.gui.GuiInfoTile;
import lach_01298.qmd.nco.multiblock.*;
import lach_01298.qmd.nco.network.multiblock.MultiblockUpdatePacket;
import lach_01298.qmd.nco.tile.TileContainerInfo;
import lach_01298.qmd.nco.tile.multiblock.*;
import lach_01298.qmd.nco.util.Lang;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public abstract class GuiMultiblockController<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & IPacketMultiblock<MULTIBLOCK, T, PACKET>, T extends ITileMultiblockPart<MULTIBLOCK, T>, PACKET extends MultiblockUpdatePacket, CONTROLLER extends TileEntity & IMultiblockGuiPart<MULTIBLOCK, T, PACKET, CONTROLLER, INFO>, INFO extends TileContainerInfo<CONTROLLER>> extends GuiInfoTile<CONTROLLER, PACKET, INFO> {
	
	protected final MULTIBLOCK multiblock;
	
	public GuiMultiblockController(Container inventory, EntityPlayer player, CONTROLLER controller, String textureLocation) {
		super(inventory, player, controller, textureLocation);
		
		this.multiblock = controller.getMultiblock();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(getGuiTexture());
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	protected abstract ResourceLocation getGuiTexture();
	
	public List<String> clearAllInfo() {
		return Lists.newArrayList(TextFormatting.ITALIC + Lang.localize("gui.nc.container.shift_clear_multiblock"));
	}
}
