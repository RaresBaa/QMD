package lach_01298.qmd.nco.block.item;

import lach_01298.qmd.nco.util.InfoHelper;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.Nullable;
import java.util.List;

public class NCItemBlock extends ItemBlock {
	
	private final TextFormatting[] fixedColors;
	private final TextFormatting infoColor;
	public final String[] fixedInfo, info;
	
	public NCItemBlock(Block block, TextFormatting[] fixedColors, String[] fixedTooltip, TextFormatting infoColor, String... tooltip) {
		super(block);
		this.fixedColors = fixedColors;
		fixedInfo = InfoHelper.buildFixedInfo(block.getTranslationKey(), fixedTooltip);
		this.infoColor = infoColor;
		info = InfoHelper.buildInfo(block.getTranslationKey(), tooltip);
	}
	
	public NCItemBlock(Block block, TextFormatting fixedColor, String[] fixedTooltip, TextFormatting infoColor, String... tooltip) {
		this(block, new TextFormatting[] {fixedColor}, fixedTooltip, infoColor, tooltip);
	}
	
	public NCItemBlock(Block block, TextFormatting infoColor, String... tooltip) {
		this(block, TextFormatting.RED, InfoHelper.EMPTY_ARRAY, infoColor, tooltip);
	}
	
	public NCItemBlock(Block block, String... tooltip) {
		this(block, TextFormatting.RED, InfoHelper.EMPTY_ARRAY, TextFormatting.AQUA, tooltip);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(itemStack, world, tooltip, flag);
		if (fixedInfo.length > 0 || info.length > 0) {
			if (fixedColors.length == 1) {
				InfoHelper.infoFull(tooltip, fixedColors[0], fixedInfo, infoColor, info);
			}
			else {
				InfoHelper.infoFull(tooltip, fixedColors, fixedInfo, infoColor, info);
			}
		}
	}
}
