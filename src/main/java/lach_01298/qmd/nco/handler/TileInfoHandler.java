package lach_01298.qmd.nco.handler;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.*;
import nc.*;
import lach_01298.qmd.nco.block.tile.info.*;
import lach_01298.qmd.nco.container.multiblock.controller.*;
import lach_01298.qmd.nco.gui.GuiInfoTileFunction;
import lach_01298.qmd.nco.gui.multiblock.controller.*;
import nc.init.NCBlocks;
import lach_01298.qmd.nco.network.tile.processor.ProcessorUpdatePacket;
import nc.tab.NCTabs;
import lach_01298.qmd.nco.tile.*;
import lach_01298.qmd.nco.tile.dummy.TileMachineInterface;
import lach_01298.qmd.nco.tile.hx.*;
import lach_01298.qmd.nco.tile.passive.TilePassive;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;

import java.util.*;
import java.util.function.Supplier;

import static nc.config.NCConfig.*;
import static lach_01298.qmd.nco.util.ContainerInfoHelper.*;

public class TileInfoHandler {
	
	public static final Object2ObjectMap<String, BlockTileInfo<?>> BLOCK_TILE_INFO_MAP = new Object2ObjectLinkedOpenHashMap<>();
	
	public static final Object2ObjectMap<String, TileContainerInfo<?>> TILE_CONTAINER_INFO_MAP = new Object2ObjectLinkedOpenHashMap<>();
	
	
	public static void preInit() {
	}
	
	public static void init() {
	}
	
	public static <T> void register(Map<String, T> map, String name, T value) {
		T prev = map.put(name, value);
		if (prev != null) {
			throw new IllegalArgumentException("Registry name \"" + name + "\" already taken for type \"" + value.getClass().getSimpleName() + "\"!");
		}
	}
	
	public static void registerBlockTileInfo(BlockTileInfo<?> info) {
		register(BLOCK_TILE_INFO_MAP, info.name, info);
	}
	
	public static void registerContainerInfo(TileContainerInfo<?> info) {
		register(TILE_CONTAINER_INFO_MAP, info.name, info);
	}



	
	@SuppressWarnings("unchecked")
	public static <TILE extends TileEntity, INFO extends BlockSimpleTileInfo<TILE>> INFO getBlockSimpleTileInfo(String name) {
		return (INFO) BLOCK_TILE_INFO_MAP.get(name);
	}

	
	@SuppressWarnings("unchecked")
	public static <TILE extends TileEntity, INFO extends TileContainerInfo<TILE>> INFO getTileContainerInfo(String name) {
		return (INFO) TILE_CONTAINER_INFO_MAP.get(name);
	}


	
	public static <TILE extends TileEntity> GuiInfoTileFunction<TILE> clientGetGuiInfoTileFunction(Supplier<GuiInfoTileFunction<TILE>> supplier) {
		return net.minecraftforge.fml.common.FMLCommonHandler.instance().getSide().isClient() ? supplier.get() : null;
	}
}
