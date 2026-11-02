package lach_01298.qmd.network;

import io.netty.buffer.ByteBuf;
import lach_01298.qmd.nco.network.tile.TileUpdatePacket;
import lach_01298.qmd.nco.tile.ITilePacket;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class QMDTileUpdatePacket extends TileUpdatePacket
{

	public QMDTileUpdatePacket()
	{

	}

	public QMDTileUpdatePacket(BlockPos pos)
	{
		this.pos = pos;

	}
	
	public SimpleNetworkWrapper getWrapper() {
		return QMDPackets.wrapper;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}

	public static class Handler extends TileUpdatePacket.Handler<QMDTileUpdatePacket, ITilePacket<QMDTileUpdatePacket>>
	{
	}

}
