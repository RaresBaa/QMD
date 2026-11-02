package lach_01298.qmd.nco.network;

import lach_01298.qmd.nco.network.*;
import lach_01298.qmd.nco.network.gui.*;
import lach_01298.qmd.nco.network.multiblock.*;
import lach_01298.qmd.nco.network.render.*;
import lach_01298.qmd.nco.network.tile.*;
import lach_01298.qmd.nco.network.tile.processor.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NCPackets {
	
	public static SimpleNetworkWrapper wrapper = null;
	
	public static void registerMessages(String channelName) {
		wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
		registerMessages();
	}
	
	public static void registerMessages() {
		// CLIENT -> SERVER
		
		wrapper.registerMessage(ClearTankPacket.Handler.class, ClearTankPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ClearFilterTankPacket.Handler.class, ClearFilterTankPacket.class, nextID(), Side.SERVER);
		
		wrapper.registerMessage(ToggleRedstoneControlPacket.Handler.class, ToggleRedstoneControlPacket.class, nextID(), Side.SERVER);
		
		wrapper.registerMessage(OpenTileGuiPacket.Handler.class, OpenTileGuiPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(OpenSideConfigGuiPacket.Handler.class, OpenSideConfigGuiPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ToggleItemSorptionPacket.Handler.class, ToggleItemSorptionPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ResetItemSorptionsPacket.Handler.class, ResetItemSorptionsPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ToggleItemOutputSettingPacket.Handler.class, ToggleItemOutputSettingPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ToggleTankSorptionPacket.Handler.class, ToggleTankSorptionPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ResetTankSorptionsPacket.Handler.class, ResetTankSorptionsPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ToggleTankOutputSettingPacket.Handler.class, ToggleTankOutputSettingPacket.class, nextID(), Side.SERVER);
		
		wrapper.registerMessage(ClearAllMaterialPacket.Handler.class, ClearAllMaterialPacket.class, nextID(), Side.SERVER);
		
		// SERVER -> CLIENT
		
		
		wrapper.registerMessage(BlockHighlightUpdatePacket.Handler.class, BlockHighlightUpdatePacket.class, nextID(), Side.CLIENT);
		
		wrapper.registerMessage(EnergyProcessorUpdatePacket.Handler.class, EnergyProcessorUpdatePacket.class, nextID(), Side.CLIENT);
		
		
		
		
		wrapper.registerMessage(HeatExchangerUpdatePacket.Handler.class, HeatExchangerUpdatePacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(HeatExchangerRenderPacket.Handler.class, HeatExchangerRenderPacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(CondenserUpdatePacket.Handler.class, CondenserUpdatePacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(CondenserRenderPacket.Handler.class, CondenserRenderPacket.class, nextID(), Side.CLIENT);
		
		
		
	}
	
	private static int packetId = 0;
	
	public static int nextID() {
		return packetId++;
	}
}
