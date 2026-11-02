package lach_01298.qmd.nco.item;

import lach_01298.qmd.nco.enumm.IMetaEnum;
import lach_01298.qmd.nco.util.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.Nullable;
import java.util.List;

public class NCItemMeta<T extends Enum<T> & IStringSerializable & IMetaEnum> extends Item implements IInfoItem, IItemMeta<T> {
	
	public final Class<T> enumm;
	public final T[] values;
	
	public final TextFormatting infoColor;
	public final String[][] tooltips;
	public String[][] info;
	
	public NCItemMeta(Class<T> enumm, TextFormatting infoColor, String[]... tooltips) {
		setHasSubtypes(true);
		this.enumm = enumm;
		values = enumm.getEnumConstants();
		this.infoColor = infoColor;
		this.tooltips = tooltips;
	}
	
	public NCItemMeta(Class<T> enumm, String[]... tooltips) {
		this(enumm, TextFormatting.AQUA, tooltips);
	}
	
	public Class<T> getEnumClass() {
		return enumm;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab)) {
			for (int i = 0; i < values.length; ++i) {
				items.add(new ItemStack(this, 1, i));
			}
		}
	}
	
	@Override
	public String getTranslationKey(ItemStack stack) {
		for (int i = 0; i < values.length; ++i) {
			if (StackHelper.getMetadata(stack) == i) {
				return getTranslationKey() + "." + values[i].getName();
			}
		}
		return getTranslationKey() + "." + values[0].getName();
	}
	
	@Override
	public void setInfo() {
		info = InfoHelper.buildInfo(getTranslationKey(), enumm, tooltips);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		int meta = StackHelper.getMetadata(stack);
		if (info.length > meta) {
			InfoHelper.infoFull(tooltip, TextFormatting.RED, InfoHelper.EMPTY_ARRAY, infoColor, info[meta]);
		}
	}
	
	protected ActionResult<ItemStack> actionResult(boolean success, ItemStack stack) {
		return new ActionResult<>(success ? EnumActionResult.SUCCESS : EnumActionResult.FAIL, stack);
	}
}
