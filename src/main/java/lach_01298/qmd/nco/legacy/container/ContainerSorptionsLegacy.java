package lach_01298.qmd.nco.legacy.container;

import lach_01298.qmd.nco.legacy.tile.*;

import lach_01298.qmd.nco.legacy.container.ContainerTileLegacy;
import lach_01298.qmd.nco.legacy.tile.ITileGuiLegacy;

public class ContainerSorptionsLegacy<T extends ITileGuiLegacy> extends ContainerTileLegacy<T> {
	
	public ContainerSorptionsLegacy(T tile) {
		super(tile);
	}
}
