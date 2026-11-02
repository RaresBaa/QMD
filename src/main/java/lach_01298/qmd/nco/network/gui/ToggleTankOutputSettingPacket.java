package lach_01298.qmd.nco.network.gui;

import io.netty.buffer.ByteBuf;
import lach_01298.qmd.nco.tile.fluid.ITileFluid;
import lach_01298.qmd.nco.tile.internal.fluid.TankOutputSetting;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class ToggleTankOutputSettingPacket extends TileGuiPacket {
	
	protected int tank;
	protected int setting;
	
	public ToggleTankOutputSettingPacket() {
		super();
	}
	
	public ToggleTankOutputSettingPacket(ITileFluid tile, int tank, TankOutputSetting setting) {
		super(tile);
		this.tank = tank;
		this.setting = setting.ordinal();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		tank = buf.readInt();
		setting = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(tank);
		buf.writeInt(setting);
	}
	
	public static class Handler extends TileGuiPacket.Handler<ToggleTankOutputSettingPacket> {
		
		@Override
		protected void onPacket(ToggleTankOutputSettingPacket message, EntityPlayerMP player, TileEntity tile) {
			if (tile instanceof ITileFluid machine) {
				TankOutputSetting setting = TankOutputSetting.values()[message.setting];
				machine.setTankOutputSetting(message.tank, setting);
				if (setting == TankOutputSetting.VOID) {
					machine.clearTank(message.tank);
				}
				machine.markDirtyAndNotify(true);
			}
		}
	}
}
