package lach_01298.qmd.nco.network.multiblock;

import io.netty.buffer.ByteBuf;
import lach_01298.qmd.nco.multiblock.hx.HeatExchanger;
import lach_01298.qmd.nco.tile.TileContainerInfo;
import lach_01298.qmd.nco.tile.hx.*;
import lach_01298.qmd.nco.tile.internal.fluid.Tank;
import lach_01298.qmd.nco.tile.internal.fluid.Tank.TankInfo;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class HeatExchangerRenderPacket extends MultiblockUpdatePacket {
	
	public List<TankInfo> shellTankInfos;
	
	public HeatExchangerRenderPacket() {
		super();
	}
	
	public HeatExchangerRenderPacket(BlockPos pos, List<Tank> shellTanks) {
		super(pos);
		shellTankInfos = TankInfo.getInfoList(shellTanks);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		shellTankInfos = readTankInfos(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		writeTankInfos(buf, shellTankInfos);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, TileHeatExchangerController, TileContainerInfo<TileHeatExchangerController>, HeatExchangerRenderPacket> {
		
		public Handler() {
			super(TileHeatExchangerController.class);
		}
		
		@Override
		protected void onPacket(HeatExchangerRenderPacket message, HeatExchanger multiblock) {
			multiblock.onRenderPacket(message);
		}
	}
}
