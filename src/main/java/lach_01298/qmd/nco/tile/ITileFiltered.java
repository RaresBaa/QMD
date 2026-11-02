package lach_01298.qmd.nco.tile;

public interface ITileFiltered extends ITile {
	
	boolean canModifyFilter(int slot);
	
	void onFilterChanged(int slot);
	
	Object getFilterKey();
}
