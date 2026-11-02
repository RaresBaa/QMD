package lach_01298.qmd.nco.block;

import lach_01298.qmd.nco.enumm.IMetaEnum;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public interface IBlockMeta<T extends Enum<T> & IStringSerializable & IMetaEnum> {
	
	Class<T> getEnumClass();
	
	T[] getValues();
	
	String getMetaName(ItemStack stack);
}
