package lach_01298.qmd.nco.item;

import lach_01298.qmd.nco.enumm.IMetaEnum;
import net.minecraft.util.IStringSerializable;

public interface IItemMeta<T extends Enum<T> & IStringSerializable & IMetaEnum> {
	
	Class<T> getEnumClass();
}
