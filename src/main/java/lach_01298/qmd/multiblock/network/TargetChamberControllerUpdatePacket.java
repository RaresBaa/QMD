package lach_01298.qmd.multiblock.network;

import io.netty.buffer.ByteBuf;
import lach_01298.qmd.nco.network.tile.TileUpdatePacket;
import lach_01298.qmd.nco.tile.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class TargetChamberControllerUpdatePacket extends TileUpdatePacket
{

	public BlockPos masterPortPos;
	public ItemStack filterStack;
	public long clusterHeatStored, clusterHeatCapacity;
	public boolean isProcessing;
	public double time, baseProcessTime;

	public TargetChamberControllerUpdatePacket()
	{

	}

	public TargetChamberControllerUpdatePacket(BlockPos pos, BlockPos masterPortPos,
			NonNullList<ItemStack> filterStacks, boolean isProcessing, double time,
			double baseProcessTime)
	{
		this.pos = pos;
		this.masterPortPos = masterPortPos;
		filterStack = filterStacks.get(0);
		clusterHeatStored = -1L;
		clusterHeatCapacity = -1L;
		this.isProcessing = isProcessing;
		this.time = time;
		this.baseProcessTime = baseProcessTime;

	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		masterPortPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		filterStack = ByteBufUtils.readItemStack(buf);
		clusterHeatStored = buf.readLong();
		clusterHeatCapacity = buf.readLong();
		isProcessing = buf.readBoolean();
		time = buf.readDouble();
		baseProcessTime = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(masterPortPos.getX());
		buf.writeInt(masterPortPos.getY());
		buf.writeInt(masterPortPos.getZ());
		ByteBufUtils.writeItemStack(buf, filterStack);
		buf.writeLong(clusterHeatStored);
		buf.writeLong(clusterHeatCapacity);
		buf.writeBoolean(isProcessing);
		buf.writeDouble(time);
		buf.writeDouble(baseProcessTime);
	}

	public static class Handler extends TileUpdatePacket.Handler<TargetChamberControllerUpdatePacket, ITilePacket<TargetChamberControllerUpdatePacket>>
	{
	}
}
