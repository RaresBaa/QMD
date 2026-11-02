package lach_01298.qmd.multiblock.network;

import io.netty.buffer.ByteBuf;
import lach_01298.qmd.network.QMDTileUpdatePacket;
import lach_01298.qmd.nco.network.tile.TileUpdatePacket;
import lach_01298.qmd.nco.tile.ITilePacket;
import lach_01298.qmd.nco.tile.internal.fluid.Tank;
import lach_01298.qmd.nco.tile.internal.fluid.Tank.TankInfo;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class AcceleratorSourceUpdatePacket extends QMDTileUpdatePacket
{
	public List<TankInfo> tanksInfo;
	
	public AcceleratorSourceUpdatePacket()
	{
		super();
	}

	public AcceleratorSourceUpdatePacket(BlockPos pos, List<Tank> tanks)
	{
		super(pos);
		tanksInfo = TankInfo.getInfoList(tanks);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		super.fromBytes(buf);
		tanksInfo = readTankInfos(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		super.toBytes(buf);
		writeTankInfos(buf, tanksInfo);
	}

	public static class Handler extends TileUpdatePacket.Handler<AcceleratorSourceUpdatePacket, ITilePacket<AcceleratorSourceUpdatePacket>>
	{
	}

}
