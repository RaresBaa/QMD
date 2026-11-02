package lach_01298.qmd.recipes;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lach_01298.qmd.QMDRadSources;
import lach_01298.qmd.block.QMDBlocks;
import lach_01298.qmd.config.QMDConfig;
import lach_01298.qmd.enums.BlockTypes.*;
import lach_01298.qmd.enums.MaterialTypes.IngotType;
import lach_01298.qmd.enums.MaterialTypes.IngotType2;
import lach_01298.qmd.enums.MaterialTypes.PartType;
import lach_01298.qmd.enums.MaterialTypes.SourceType;
import lach_01298.qmd.item.IItemParticleAmount;
import lach_01298.qmd.item.QMDItems;
import lach_01298.qmd.recipe.QMDRecipe;
import lach_01298.qmd.recipe.QMDRecipeHandler;
import nc.config.NCConfig;
import nc.enumm.MetaEnums;
import nc.init.NCItems;
import nc.radiation.RadSources;
import lach_01298.qmd.nco.recipe.BasicRecipe;
import lach_01298.qmd.nco.recipe.BasicRecipeHandler;
import nc.recipe.NCRecipes;
import lach_01298.qmd.nco.recipe.ingredient.*;
import lach_01298.qmd.nco.util.FluidRegHelper;
import lach_01298.qmd.nco.util.NCMath;
import lach_01298.qmd.nco.util.OreDictHelper;
import lach_01298.qmd.nco.util.StringHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static lach_01298.qmd.config.QMDConfig.*;
import static nc.config.NCConfig.ore_dict_raw_material_recipes;
import static lach_01298.qmd.nco.recipe.AbstractRecipeHandler.*;
import static lach_01298.qmd.nco.util.FluidStackHelper.*;


public class QMDRecipes
{
	private static boolean initialized = false;
	private static final Object2ObjectMap<String, QMDRecipeHandler> RECIPE_HANDLER_MAP = new Object2ObjectOpenHashMap();
	private static final Object2ObjectMap<String, BasicRecipeHandler> BASIC_RECIPE_HANDLER_MAP = new Object2ObjectOpenHashMap();

	public static AcceleratorSourceRecipes accelerator_source;
	public static AcceleratorCoolingRecipes accelerator_cooling;
	public static MassSpectrometerRecipes mass_spectrometer;

	public static LiquefierRecipes liquefier;
	public static LiquefierCoolantRecipes liquefier_coolant;

	public static TargetChamberRecipes target_chamber;
	public static DecayChamberRecipes decay_chamber;
	public static BeamDumpRecipes beam_dump;
	public static CollisionChamberRecipes collision_chamber;

	public static OreLeacherRecipes ore_leacher;
	public static IrradiatorRecipes irradiator;
	public static IrradiatorFuel irradiator_fuel;

	public static NeutralContainmentRecipes neutral_containment;
	public static CellFillingRecipes cell_filling;
	public static NucleosynthesisChamberRecipes nucleosynthesis_chamber;
	public static VacuumChamberHeaterRecipes vacuum_chamber_heating;

	public static void putHandler(QMDRecipeHandler handler)
	{
		RECIPE_HANDLER_MAP.put(handler.getName(), handler);
	}

	public static void putBasicHandler(BasicRecipeHandler handler)
	{
		BASIC_RECIPE_HANDLER_MAP.put(handler.getName(), handler);
	}

	public static <T extends QMDRecipeHandler> T getHandler(String name)
	{
		return (T) RECIPE_HANDLER_MAP.get(name);
	}

	public static <T extends BasicRecipeHandler> T getBasicHandler(String name)
	{
		return (T) BASIC_RECIPE_HANDLER_MAP.get(name);
	}

	public static Collection<QMDRecipeHandler> getHandlers()
	{
		return RECIPE_HANDLER_MAP.values();
	}

	public static Collection<BasicRecipeHandler> getBasicHandlers()
	{
		return BASIC_RECIPE_HANDLER_MAP.values();
	}

	public static List<QMDRecipe> getRecipeList(String name)
	{
		return getHandler(name).getRecipeList();
	}

	public static List<BasicRecipe> getBasicRecipeList(String name)
	{
		return getBasicHandler(name).getRecipeList();
	}

	public static List<Set<String>> getValidFluids(String name)
	{
		return getHandler(name).validFluids;
	}

	public static List<Set<String>> getBasicValidFluids(String name)
	{
		return getBasicHandler(name).validFluids;
	}

	public static final String[] RECIPE_HANDLER_NAME_ARRAY = new String[]
			{
					"accelerator_source",
					"accelerator_cooling",
					"mass_spectrometer",

					"liquefier",

					"target_chamber",
					"decay_chamber",
					"beam_dump",
					"collision_chamber",

					"ore_leacher",
					"irradiator",
					"irradiator_fuel",

					"neutral_containment",
					"cell_filling",
					"nucleosynthesis_chamber",
					"vacuum_chamber_heating"
			};

	public static final String[] BASIC_RECIPE_HANDLER_NAME_ARRAY = new String[]
			{
					"liquefier_coolant"
			};

	@SubscribeEvent(priority = EventPriority.LOW)
	public void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
		if (initialized)
			return;

		putHandler(new AcceleratorSourceRecipes());
		putHandler(new AcceleratorCoolingRecipes());
		putHandler(new MassSpectrometerRecipes());

		putHandler(new LiquefierRecipes());
		putBasicHandler(new LiquefierCoolantRecipes());

		putHandler(new TargetChamberRecipes());
		putHandler(new DecayChamberRecipes());
		putHandler(new BeamDumpRecipes());
		putHandler(new CollisionChamberRecipes());

		putHandler(new OreLeacherRecipes());
		putHandler(new IrradiatorRecipes());
		putHandler(new IrradiatorFuel());

		putHandler(new NeutralContainmentRecipes());
		putHandler(new CellFillingRecipes());
		putHandler(new NucleosynthesisChamberRecipes());
		putHandler(new VacuumChamberHeaterRecipes());

		this.registerShortcuts();
		addRecipes();

		initialized = true;
	}

	public void registerShortcuts()
	{
		accelerator_source = (AcceleratorSourceRecipes) getHandler("accelerator_source");
		accelerator_cooling = (AcceleratorCoolingRecipes) getHandler("accelerator_cooling");
		mass_spectrometer = (MassSpectrometerRecipes) getHandler("mass_spectrometer");

		liquefier = (LiquefierRecipes) getHandler("liquefier");
		liquefier_coolant = (LiquefierCoolantRecipes) getBasicHandler("liquefier_coolant");

		target_chamber = (TargetChamberRecipes) getHandler("target_chamber");
		decay_chamber = (DecayChamberRecipes) getHandler("decay_chamber");
		beam_dump = (BeamDumpRecipes) getHandler("beam_dump");
		collision_chamber = (CollisionChamberRecipes) getHandler("collision_chamber");

		ore_leacher = (OreLeacherRecipes) getHandler("ore_leacher");
		irradiator = (IrradiatorRecipes) getHandler("irradiator");
		irradiator_fuel = (IrradiatorFuel) getHandler("irradiator_fuel");

		neutral_containment = (NeutralContainmentRecipes) getHandler("neutral_containment");
		cell_filling = (CellFillingRecipes) getHandler("cell_filling");
		nucleosynthesis_chamber = (NucleosynthesisChamberRecipes) getHandler("nucleosynthesis_chamber");
		vacuum_chamber_heating = (VacuumChamberHeaterRecipes) getHandler("vacuum_chamber_heating");

	}


	public static void init()
	{
		for (QMDRecipeHandler handler : getHandlers())
		{
			handler.init();
		}
		for (BasicRecipeHandler handler : getBasicHandlers())
		{
			handler.init();
		}
	}

	public static void postInit()
	{
		for (QMDRecipeHandler handler : getHandlers())
		{
			handler.postInit();
		}
		for (BasicRecipeHandler handler : getBasicHandlers())
		{
			handler.postInit();
		}
	}

	public static void refreshRecipeCaches()
	{
		for (QMDRecipeHandler handler : getHandlers())
		{
			handler.refreshCache();
		}
		for (BasicRecipeHandler handler : getBasicHandlers())
		{
			handler.refreshCache();
		}
	}



	public static final List<String> PLASTIC_TYPES = Lists.newArrayList("bioplastic", "sheetPlastic");

	public static void addRecipes()
	{
		// Alloy furnace
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Steel", 5, "Chromium", 1, "StainlessSteel", 6, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Niobium", 3, "Tin", 1, "NiobiumTin", 4, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Niobium", 1, "Titanium", 1, "NiobiumTitanium", 2, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Tungsten", 1, "Graphite", 1, "TungstenCarbide", 2, 2D, 2D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Osmium", 1, "Iridium", 1, "Osmiridium", 2, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Nickel", 1, "Chromium", 1, "Nichrome", 2, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Nichrome", 2, "NiobiumTitanium", 1, "SuperAlloy", 3, 1D, 1D);
		NCBridge.add(NCRecipes.alloy_furnace, "dustZinc", "dustSulfur", "dustZincSulfide", 1D, 1D);


		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Uranium238", 9, "Uranium235", 1, "Uranium", 10, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Boron11", 9, "Boron10", 3, "Boron", 12, 1D, 1D);
		NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Lithium7", 9, "Lithium6", 1, "Lithium", 10, 1D, 1D);
		//NCRecipes.alloy_furnace.addAlloyIngotIngotRecipes("Magnesium24", 8, "Magnesium26", 1, "Magnesium", 9, 1D, 1D);

		// Fluid Infuser
		NCBridge.add(NCRecipes.infuser, new ItemStack(QMDItems.part, 1, PartType.DETECTOR_CASING.getID()), fluidStack("liquid_hydrogen", BUCKET_VOLUME), new ItemStack(QMDBlocks.particleChamberDetector, 1, DetectorType.BUBBLE_CHAMBER.getID()), 1D, 1D);
		NCBridge.add(NCRecipes.infuser, new ItemStack(QMDItems.part, 1, PartType.WIRE_CHAMBER_CASING.getID()), fluidStack("argon", BUCKET_VOLUME), new ItemStack(QMDBlocks.particleChamberDetector, 1, DetectorType.WIRE_CHAMBER.getID()), 1D, 1D);

		NCBridge.add(NCRecipes.infuser, new ItemStack(QMDItems.part, 1, PartType.EMPTY_COOLER.getID()), fluidStack("water", BUCKET_VOLUME), new ItemStack(QMDBlocks.acceleratorCooler1, 1, CoolerType1.WATER.getID()), 1D, 1D);
		NCBridge.add(NCRecipes.infuser, new ItemStack(QMDItems.part, 1, PartType.EMPTY_COOLER.getID()), fluidStack("liquidhelium", BUCKET_VOLUME), new ItemStack(QMDBlocks.acceleratorCooler2, 1, CoolerType2.LIQUID_HELIUM.getID()), 1D, 1D);
		NCBridge.add(NCRecipes.infuser, new ItemStack(QMDItems.part, 1, PartType.EMPTY_COOLER.getID()), fluidStack("liquid_nitrogen", BUCKET_VOLUME), new ItemStack(QMDBlocks.acceleratorCooler2, 1, CoolerType2.LIQUID_NITROGEN.getID()), 1D, 1D);
		NCBridge.add(NCRecipes.infuser, new ItemStack(QMDItems.part, 1, PartType.EMPTY_COOLER.getID()), fluidStack("cryotheum", BUCKET_VOLUME), new ItemStack(QMDBlocks.acceleratorCooler2, 1, CoolerType2.CRYOTHEUM.getID()), 1D, 1D);
		NCBridge.add(NCRecipes.infuser, new ItemStack(QMDItems.part, 1, PartType.EMPTY_COOLER.getID()), fluidStack("enderium", INGOT_VOLUME * 4), new ItemStack(QMDBlocks.acceleratorCooler2, 1, CoolerType2.ENDERIUM.getID()), 1D, 1D);

		NCBridge.add(NCRecipes.infuser, new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.EMPTY.getID()), fluidStack("hydrogen", BUCKET_VOLUME / 4), new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.HYDROGEN.getID()), 1D, 1D);
		NCBridge.add(NCRecipes.infuser, new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.EMPTY.getID()), fluidStack("helium", BUCKET_VOLUME / 4), new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.HELIUM.getID()), 1D, 1D);
		NCBridge.add(NCRecipes.infuser, new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.EMPTY.getID()), fluidStack("nitrogen", BUCKET_VOLUME / 4), new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.NITROGEN.getID()), 1D, 1D);
		NCBridge.add(NCRecipes.infuser, new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.EMPTY.getID()), fluidStack("oxygen", BUCKET_VOLUME / 4), new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.OXYGEN.getID()), 1D, 1D);
		NCBridge.add(NCRecipes.infuser, new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.EMPTY.getID()), fluidStack("neon", BUCKET_VOLUME / 4), new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.NEON.getID()), 1D, 1D);
		NCBridge.add(NCRecipes.infuser, new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.EMPTY.getID()), fluidStack("argon", BUCKET_VOLUME / 4), new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.ARGON.getID()), 1D, 1D);
		NCBridge.add(NCRecipes.infuser, new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.ARGON.getID()), fluidStack("sodium", INGOT_VOLUME), new ItemStack(QMDBlocks.dischargeLamp2, 1, LampType2.SODIUM.getID()), 1D, 1D);
		NCBridge.add(NCRecipes.infuser, new ItemStack(QMDBlocks.dischargeLamp, 1, LampType.ARGON.getID()), fluidStack("mercury", INGOT_VOLUME), new ItemStack(QMDBlocks.dischargeLamp2, 1, LampType2.MERCURY.getID()), 1D, 1D);

		NCBridge.add(NCRecipes.infuser, "dustStrontium", fluidStack("hydrochloric_acid", BUCKET_VOLUME * 2), "dustStrontiumChloride", 1D, 1D);
		NCBridge.add(NCRecipes.infuser, "dustIron", fluidStack("hydrofluoric_acid", 1500), "dustIronFluoride", 1D, 1D);
		NCBridge.add(NCRecipes.infuser, "ingotIron", fluidStack("hydrofluoric_acid", 1500), "dustIronFluoride", 1D, 1D);

		NCRecipes.infuser.addOxidizingRecipe("dustCopper", BUCKET_VOLUME);
		NCRecipes.infuser.addOxidizingRecipe("dustTungsten", BUCKET_VOLUME);
		NCRecipes.infuser.addOxidizingRecipe("dustHafnium", BUCKET_VOLUME);


		// Fluid Enricher
		NCBridge.add(NCRecipes.dissolver, "dustTungstenOxide", fluidStack("sodium_hydroxide_solution", GEM_VOLUME * 2), fluidStack("sodium_tungstate_solution", GEM_VOLUME), 1D, 1D);
		NCBridge.add(NCRecipes.dissolver, "dustLead", fluidStack("nitric_acid", GEM_VOLUME * 2), fluidStack("lead_nitrate_solution", GEM_VOLUME), 1D, 1D);
		NCBridge.add(NCRecipes.dissolver, "ingotYttrium", fluidStack("alumina", 120), fluidStack("yag", 48), 2D, 2D);
		NCBridge.add(NCRecipes.dissolver, "ingotNeodymium", fluidStack("yag", INGOT_BLOCK_VOLUME), fluidStack("nd_yag", INGOT_BLOCK_VOLUME), 2D, 2D);

		NCBridge.add(NCRecipes.dissolver, "dustSalt", fluidStack("water", BUCKET_VOLUME), fluidStack("sodium_chloride_solution", GEM_VOLUME), 1D, 1D);


		// Chemical reactor
		NCBridge.add(NCRecipes.chemical_reactor, fluidStack("sodium_tungstate_solution", GEM_VOLUME), fluidStack("lead_nitrate_solution", GEM_VOLUME), fluidStack("lead_tungstate_solution", GEM_VOLUME), fluidStack("sodium_nitrate_solution", GEM_VOLUME), 1D, 1D);


		NCBridge.add(NCRecipes.chemical_reactor, fluidStack("hydrogen", BUCKET_VOLUME), fluidStack("chlorine", BUCKET_VOLUME), fluidStack("hydrochloric_acid", 2 * BUCKET_VOLUME), new EmptyFluidIngredient(), 1D, 1D);
		NCBridge.add(NCRecipes.chemical_reactor, fluidStack("liquidhydrogenchloride", BUCKET_VOLUME), fluidStack("water", BUCKET_VOLUME), fluidStack("hydrochloric_acid", BUCKET_VOLUME), new EmptyFluidIngredient(), 1D, 1D);
		NCBridge.add(NCRecipes.chemical_reactor, fluidStack("nitrogen", BUCKET_VOLUME), fluidStack("oxygen", BUCKET_VOLUME), fluidStack("nitric_oxide", BUCKET_VOLUME * 2), new EmptyFluidIngredient(), 1D, 1D);
		NCBridge.add(NCRecipes.chemical_reactor, fluidStack("nitric_oxide", BUCKET_VOLUME * 2), fluidStack("oxygen", BUCKET_VOLUME), fluidStack("nitrogen_dioxide", BUCKET_VOLUME * 2), new EmptyFluidIngredient(), 1D, 1D);
		NCBridge.add(NCRecipes.chemical_reactor, fluidStack("nitrogen_dioxide", BUCKET_VOLUME * 3), fluidStack("water", BUCKET_VOLUME), fluidStack("nitric_acid", BUCKET_VOLUME * 2), fluidStack("nitric_oxide", BUCKET_VOLUME), 1D, 1D);
		NCBridge.add(NCRecipes.chemical_reactor, fluidStack("sodium_hydroxide_solution", GEM_VOLUME), fluidStack("nitric_acid", BUCKET_VOLUME), fluidStack("sodium_nitrate_solution", GEM_VOLUME), new EmptyFluidIngredient(), 1D, 1D);

		// Separator
		//NCRecipes.separator.addRecipe(oreStackList(Lists.newArrayList("ingotMagnesium", "dustMagnesium"), 9), oreStack("ingotMagnesium24", 8), oreStack("ingotMagnesium26", 1), 6D, 1D);
		NCBridge.add(NCRecipes.isotope_separator, oreStackList(Lists.newArrayList("ingotCalcium", "dustCalcium"), 8), oreStack("ingotCalcium48", 1), new EmptyItemIngredient(), 6D, 1D);

		// Centrifuge
		NCBridge.addCentrifuge(NCRecipes.centrifuge, fluidStack("redstone", REDSTONE_DUST_VOLUME), fluidStack("mercury", INGOT_VOLUME), fluidStack("sulfur", GEM_VOLUME), new EmptyFluidIngredient(), new EmptyFluidIngredient(), new EmptyFluidIngredient(), new EmptyFluidIngredient(), 1D, 1D);
		NCBridge.addCentrifuge(NCRecipes.centrifuge, fluidStack("coal", COAL_DUST_VOLUME), fluidStack("carbon", COAL_DUST_VOLUME), fluidStack("sulfur", GEM_VOLUME / 6), new EmptyFluidIngredient(), new EmptyFluidIngredient(), new EmptyFluidIngredient(), new EmptyFluidIngredient(), 1D, 1D);

		// fluid mixer
		NCBridge.add(NCRecipes.salt_mixer, fluidStack("mercury", INGOT_VOLUME), fluidStack("sulfur", GEM_VOLUME), fluidStack("redstone", REDSTONE_DUST_VOLUME), 1D, 1D);


		//Electrolyzer
		NCBridge.add(NCRecipes.electrolyser, fluidStack("sodium_chloride", GEM_VOLUME), fluidStack("sodium", INGOT_VOLUME), fluidStack("chlorine", BUCKET_VOLUME / 2), new EmptyFluidIngredient(), new EmptyFluidIngredient(), 1D, 1D);
		NCBridge.add(NCRecipes.electrolyser, fluidStack("sodium_chloride_solution", 2 * GEM_VOLUME), fluidStack("hydrogen", BUCKET_VOLUME), fluidStack("chlorine", BUCKET_VOLUME), fluidStack("sodium_hydroxide_solution", GEM_VOLUME * 2), new EmptyFluidIngredient(), 0.5D, 1D);


		// Manufactory
		NCBridge.add(NCRecipes.manufactory, "bouleSilicon", oreStack("waferSilicon", 4), 1D, 1D);


		// Melter
		NCBridge.add(NCRecipes.melter, "ingotMercury", fluidStack("mercury", INGOT_VOLUME), 0.5D, 0D);
		NCBridge.add(NCRecipes.melter, "dustIodine", fluidStack("iodine", INGOT_VOLUME));
		NCBridge.add(NCRecipes.melter, "dustSamarium", fluidStack("samarium", INGOT_VOLUME));
		NCBridge.add(NCRecipes.melter, "dustTerbium", fluidStack("terbium", INGOT_VOLUME));
		NCBridge.add(NCRecipes.melter, "dustErbium", fluidStack("erbium", INGOT_VOLUME));
		NCBridge.add(NCRecipes.melter, "dustYtterbium", fluidStack("ytterbium", INGOT_VOLUME));
		NCBridge.add(NCRecipes.melter, "dustMolybdenum", fluidStack("molybdenum", INGOT_VOLUME));
		NCBridge.add(NCRecipes.melter, "dustBismuth", fluidStack("bismuth", INGOT_VOLUME));
		NCBridge.add(NCRecipes.melter, "dustPolonium", fluidStack("polonium", INGOT_VOLUME));
		NCBridge.add(NCRecipes.melter, "dustRadium", fluidStack("radium", INGOT_VOLUME));


		if (FluidRegHelper.fluidExists("brine") && QMDConfig.override_nc_recipes)
		{
			List<IFluidIngredient> fluidIngredients = new ArrayList<IFluidIngredient>();
			List<IItemIngredient> itemIngredients = new ArrayList<IItemIngredient>();
			itemIngredients.add(oreStack("dustSalt", 1));
			NCBridge.remove(NCRecipes.melter, itemIngredients, fluidIngredients);
			itemIngredients.add(oreStack("itemSalt", 1));
			NCBridge.remove(NCRecipes.melter, itemIngredients, fluidIngredients);

			NCBridge.add(NCRecipes.melter, "dustSalt", fluidStack("sodium_chloride", GEM_VOLUME));
		}
		else if (!FluidRegHelper.fluidExists("brine"))
		{
			NCBridge.add(NCRecipes.melter, "dustSalt", fluidStack("sodium_chloride", GEM_VOLUME));
		}


		if (QMDConfig.override_nc_recipes)
		{
			List<IFluidIngredient> fluidIngredients = new ArrayList<IFluidIngredient>();
			List<IItemIngredient> itemIngredients = new ArrayList<IItemIngredient>();
			itemIngredients.add(oreStack("dustGraphite", 1));

			NCBridge.remove(NCRecipes.melter, itemIngredients, fluidIngredients);

			itemIngredients = new ArrayList<IItemIngredient>();
			itemIngredients.add(oreStack("dustGraphite", 1));

			NCBridge.remove(NCRecipes.melter, itemIngredients, fluidIngredients);
			NCBridge.add(NCRecipes.melter, oreStackList(Lists.newArrayList("dustGraphite", "ingotGraphite"), 1), fluidStack("carbon", COAL_DUST_VOLUME));

			itemIngredients = new ArrayList<IItemIngredient>();
			itemIngredients.add(oreStack("blockGraphite", 1));

			NCBridge.remove(NCRecipes.melter, itemIngredients, fluidIngredients);
			NCBridge.add(NCRecipes.melter, "blockGraphite", fluidStack("carbon", COAL_BLOCK_VOLUME));

		}
		NCBridge.add(NCRecipes.melter, oreStackList(Lists.newArrayList("dustCharcoal", "charcoal"), 1), fluidStack("carbon", COAL_DUST_VOLUME));


		//ingot former
		NCBridge.add(NCRecipes.ingot_former, fluidStack("carbon", COAL_DUST_VOLUME), "ingotGraphite");

		if (QMDConfig.override_nc_recipes)
		{
			List<IFluidIngredient> fluidIngredients = new ArrayList<IFluidIngredient>();
			List<IItemIngredient> itemIngredients = new ArrayList<IItemIngredient>();

			fluidIngredients.add(fluidStack("coal", COAL_DUST_VOLUME));

			NCBridge.remove(NCRecipes.ingot_former, itemIngredients, fluidIngredients);
			NCBridge.add(NCRecipes.ingot_former, fluidStack("coal", COAL_DUST_VOLUME), "coal", 0.5D, 1D);
		}

		// Crystallizer
		NCBridge.add(NCRecipes.crystallizer, fluidStack("silicon", INGOT_BLOCK_VOLUME), "bouleSilicon", 2D, 2D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("lead_tungstate_solution", GEM_VOLUME), new ItemStack(QMDItems.part, 8, PartType.SCINTILLATOR_PWO.getID()), 1D, 1D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("sodium_nitrate_solution", GEM_VOLUME), "dustSodiumNitrate", 1D, 1D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("sodium_chloride_solution", GEM_VOLUME), "dustSalt", 1D, 1D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("nd_yag", INGOT_VOLUME * 3), "rodNdYAG", 2D, 2D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("salt_water", BUCKET_VOLUME * 10), "dustSalt", 2D, 4D);


		NCBridge.add(NCRecipes.crystallizer, fluidStack("iodine", INGOT_VOLUME), "dustIodine", 0.25D, 0D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("samarium", INGOT_VOLUME), "dustSamarium", 0.25D, 0D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("terbium", INGOT_VOLUME), "dustTerbium", 0.25D, 0D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("erbium", INGOT_VOLUME), "dustErbium", 0.25D, 0D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("ytterbium", INGOT_VOLUME), "dustYtterbium", 0.25D, 0D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("molybdenum", INGOT_VOLUME), "dustMolybdenum", 0.25D, 0D);

		NCBridge.add(NCRecipes.crystallizer, fluidStack("bismuth", INGOT_VOLUME), "dustBismuth", 0.25D, 0D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("radium", INGOT_VOLUME), "dustRadium", 0.25D, 0D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("polonium", INGOT_VOLUME), "dustPolonium", 0.25D, 0D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("europium_155", INGOT_VOLUME), "dustEuropium155", 0.25D, 0D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("ruthenium_106", INGOT_VOLUME), "dustRuthenium106", 0.25D, 0D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("strontium_90", INGOT_VOLUME), "dustStrontium90", 0.25D, 0D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("promethium_147", INGOT_VOLUME), "dustPromethium147", 0.25D, 0D);
		NCBridge.add(NCRecipes.crystallizer, fluidStack("caesium_137", INGOT_VOLUME), "dustCaesium137", 0.25D, 0D);

		// Pressurizer
		NCBridge.add(NCRecipes.pressurizer, (oreStack("dustStrontium90", 9)), "blockStrontium90", 1D, 2D);


		// SuperCooler
		if (QMDConfig.override_nc_recipes)
		{
			List<IItemIngredient> emptyitems = new ArrayList<IItemIngredient>();
			List<IFluidIngredient> helium = new ArrayList<IFluidIngredient>();
			helium.add(fluidStack("helium", BUCKET_VOLUME * 8));
			List<IFluidIngredient> nitrogen = new ArrayList<IFluidIngredient>();
			nitrogen.add(fluidStack("nitrogen", BUCKET_VOLUME * 8));

			NCBridge.remove(NCRecipes.supercooler, emptyitems, helium);
			NCBridge.remove(NCRecipes.supercooler, emptyitems, nitrogen);

			NCBridge.add(NCRecipes.supercooler, fluidStack("helium", 64), fluidStack("liquidhelium", 1), 2D / 150D, 5D);
			NCBridge.add(NCRecipes.supercooler, fluidStack("nitrogen", 64), fluidStack("liquid_nitrogen", 1), 2D / 150D, 2.5D);

			NCBridge.add(NCRecipes.supercooler, fluidStack("hydrogen", 64), fluidStack("liquid_hydrogen", 1), 2D / 150D, 3.75D);
			NCBridge.add(NCRecipes.supercooler, fluidStack("neon", 64), fluidStack("liquid_neon", 1), 2D / 150D, 3.75D);
			NCBridge.add(NCRecipes.supercooler, fluidStack("argon", 64), fluidStack("liquid_argon", 1), 2D / 150D, 2.5D);
			NCBridge.add(NCRecipes.supercooler, fluidStack("oxygen", 64), fluidStack("liquid_oxygen", 1), 2D / 150D, 2.5D);
			NCBridge.add(NCRecipes.supercooler, fluidStack("compressed_air", 64), fluidStack("liquid_air", 1), 2D / 150D, 1D);

		}
		else
		{
			NCBridge.add(NCRecipes.supercooler, fluidStack("hydrogen", BUCKET_VOLUME * 8), fluidStack("liquid_hydrogen", 25), 1D, 1D);
			NCBridge.add(NCRecipes.supercooler, fluidStack("neon", BUCKET_VOLUME * 8), fluidStack("liquid_neon", 25), 1D, 1D);
			NCBridge.add(NCRecipes.supercooler, fluidStack("argon", BUCKET_VOLUME * 8), fluidStack("liquid_argon", 25), 0.5D, 0.5D);
			NCBridge.add(NCRecipes.supercooler, fluidStack("oxygen", BUCKET_VOLUME * 8), fluidStack("liquid_oxygen", 25), 0.5D, 0.5D);
			NCBridge.add(NCRecipes.supercooler, fluidStack("compressed_air", BUCKET_VOLUME * 8), fluidStack("liquid_air", 25), 0.5D , 0.5D);
		}


		// Decay Hastener
		NCBridge.add(NCRecipes.decay_hastener, "ingotBeryllium7", "ingotLithium7", getDecayHastenerTimeMultipler(QMDRadSources.BERYLLIUM_7), 1D, QMDRadSources.BERYLLIUM_7);
		NCBridge.add(NCRecipes.decay_hastener, "ingotCobalt60", "dustNickel", getDecayHastenerTimeMultipler(QMDRadSources.COBALT_60), 1d, QMDRadSources.COBALT_60);
		NCBridge.add(NCRecipes.decay_hastener, "ingotIridium192", "dustPlatinum", getDecayHastenerTimeMultipler(QMDRadSources.IRIDIUM_192), 1d, QMDRadSources.IRIDIUM_192);

		// Decay Pool




		// Assembler
		NCBridge.addAssembler(oreStack("dustBSCCO", 3), oreStack("ingotSilver", 6), new EmptyItemIngredient(), new EmptyItemIngredient(), oreStack("wireBSCCO", 6), 1D, 1D);
		NCBridge.addAssembler(oreStack("dustSSFAF", 3), oreStack("ingotSilver", 6), new EmptyItemIngredient(), new EmptyItemIngredient(), oreStack("wireSSFAF", 6), 1D, 1D);
		NCBridge.addAssembler(oreStack("dustYBCO", 3), oreStack("ingotSilver", 6), new EmptyItemIngredient(), new EmptyItemIngredient(), oreStack("wireYBCO", 6), 1D, 1D);
		NCBridge.addAssembler(oreStack("dustHBCCO", 3), oreStack("ingotSilver", 6), new EmptyItemIngredient(), new EmptyItemIngredient(), oreStack("wireHBCCO", 6), 1D, 1D);
		NCBridge.addAssembler(oreStack("ingotTungsten", 4),oreStack("ingotGold", 2), new EmptyItemIngredient(), new EmptyItemIngredient(), oreStack("wireGoldTungsten", 6), 1D, 1D);
		NCBridge.addAssembler(oreStack("dustBismuth", 2), oreStack("dustStrontium", 2), oreStack("dustCalcium", 2), oreStack("dustCopperOxide", 3), oreStack("dustBSCCO", 3), 1D, 1D);
		NCBridge.addAssembler(oreStack("dustSamarium", 1), oreStack("dustStrontium", 1), oreStack("dustIronFluoride", 2), oreStack("dustArsenic", 2), oreStack("dustSSFAF", 3), 1D, 1D);
		NCBridge.addAssembler(oreStack("dustYttrium", 1), oreStack("dustBarium", 2), oreStack("dustCopperOxide", 3), new EmptyItemIngredient(), oreStack("dustYBCO", 3), 1D, 1D);
		NCBridge.addAssembler(oreStack("ingotMercury", 1), oreStack("dustBarium", 2), oreStack("dustCalcium", 2) , oreStack("dustCopperOxide", 3), oreStack("dustHBCCO", 3), 1D, 1D);


		NCBridge.addAssembler(oreStackList(PLASTIC_TYPES, 2), oreStack("dyeBlue", 1), new EmptyItemIngredient(), new EmptyItemIngredient(), new ItemStack(QMDItems.part, 1, PartType.SCINTILLATOR_PLASTIC.getID()), 1D, 1D);
		NCBridge.addAssembler("siliconNDoped", oreStack("dustRedstone", 4), "ingotGold", "ingotSilver", "processorBasic", 1D, 1D);
		NCBridge.addAssembler("processorBasic", oreStack("dustRedstone", 4), "dustHafniumOxide", "siliconPDoped", "processorAdvanced", 1D, 1D);
		NCBridge.addAssembler("processorAdvanced", oreStack("wireBSCCO", 4), "dustHafniumOxide", "ingotPlatinum", "processorElite", 1D, 1D);
		NCBridge.addAssembler(oreStack("ingotTungsten", 2), new EmptyItemIngredient(), new EmptyItemIngredient(), new EmptyItemIngredient(), IItemParticleAmount.fullItem(new ItemStack(QMDItems.source, 1, SourceType.TUNGSTEN_FILAMENT.getID())), 1D, 1D);
		NCBridge.addAssembler(oreStack("ingotFerroboron", 2), "ingotNeodymium", new EmptyItemIngredient(), new EmptyItemIngredient(), "magnetNeodymium", 1D, 1D);
		NCBridge.addAssembler("dustPotassium", "dustIodine", new ItemStack(Items.SUGAR, 4), "bioplastic", new ItemStack(QMDItems.potassiumIodineTablet, 4), 1D, 1D);
		NCBridge.addAssembler(oreStack("ingotCobalt", 2), "dustSamarium", new EmptyItemIngredient(), new EmptyItemIngredient(), "magnetSamariumCobalt", 1D, 1D);

		//Fission Irradiator
		//NCRecipes.fission_irradiator.addRecipe(FluidUtil.getFilledBucket(fluidStack("deuterium", 1000).getStack()), FluidUtil.getFilledBucket(fluidStack("tritium", 1000).getStack()),60000,0d,0); //1920000


		//fuel reprocessor
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteFissionLight", chanceOreStack("dustTin", 1, 19), chanceOreStack("dustSilver", 1, 10), chanceOreStack("dustPalladium", 1, 32), chanceOreStack("dustMolybdenum", 1, 56), chanceOreStack("dustZirconium", 1, 48), chanceOreStack("dustYttrium", 1, 11), chanceOreStack("dustStrontium90", 1, 10), chanceOreStack("dustStrontium", 1, 14));
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteFissionHeavy", chanceOreStack("dustNeodymium", 1, 17), chanceOreStack("dustBarium", 1, 11), chanceOreStack("dustTin", 1, 59), chanceOreStack("dustSilver", 1, 9), chanceOreStack("dustPalladium", 1, 29), chanceOreStack("dustRuthenium106", 1, 9), chanceOreStack("dustMolybdenum", 1, 37), chanceOreStack("dustZirconium", 1, 29));

		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationCalifornium", chanceOreStack("dustPolonium", 1, 10), chanceOreStack("dustBismuth", 1, 15), chanceOreStack("dustLead", 1, 41), chanceOreStack("ingotMercury", 1, 15), chanceOreStack("dustPlatinum", 1, 10), chanceOreStack("dustOsmium", 1, 9),new EmptyItemIngredient(),new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationBerkelium", chanceOreStack("dustRadium", 1, 15), chanceOreStack("dustPolonium", 1, 18), chanceOreStack("dustBismuth", 1, 12), chanceOreStack("dustLead", 1, 31), chanceOreStack("ingotMercury", 1, 11),chanceOreStack("dustPlatinum", 1, 13),new EmptyItemIngredient(),new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationCurium", chanceOreStack("dustPolonium", 1, 10), chanceOreStack("dustBismuth", 1, 16), chanceOreStack("dustLead", 1, 37), chanceOreStack("ingotMercury", 1, 15), chanceOreStack("dustPlatinum", 1, 11), chanceOreStack("dustOsmium", 1, 11),new EmptyItemIngredient(),new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationAmericium", chanceOreStack("dustPolonium", 1, 9), chanceOreStack("dustBismuth", 1, 15), chanceOreStack("dustLead", 1, 38), chanceOreStack("ingotMercury", 1, 15), chanceOreStack("dustPlatinum", 1, 11), chanceOreStack("dustOsmium", 1, 12),new EmptyItemIngredient(),new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationPlutonium", chanceOreStack("dustPolonium", 1, 9),chanceOreStack("dustBismuth", 1, 15), chanceOreStack("dustLead", 1, 36), chanceOreStack("ingotMercury", 1, 15), chanceOreStack("dustPlatinum", 1, 12), chanceOreStack("dustOsmium", 1, 13), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationNeptunium", chanceOreStack("dustBismuth", 1, 15), chanceOreStack("dustLead", 1, 35), chanceOreStack("ingotMercury", 1, 15), chanceOreStack("dustPlatinum", 1, 12), chanceOreStack("dustOsmium", 1, 14),chanceOreStack("dustTungsten", 1, 9), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationUranium", chanceOreStack("dustBismuth", 1, 15), chanceOreStack("dustLead", 1, 35), chanceOreStack("ingotMercury", 1, 15), chanceOreStack("dustPlatinum", 1, 12), chanceOreStack("dustOsmium", 1, 14), chanceOreStack("dustTungsten", 1, 9), new EmptyItemIngredient(),new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationProtactinium", chanceOreStack("dustBismuth", 1, 13), chanceOreStack("dustLead", 1, 32), chanceOreStack("ingotMercury", 1, 16), chanceOreStack("dustPlatinum", 1, 12), chanceOreStack("dustOsmium", 1, 15), chanceOreStack("dustHafnium", 1, 12), new EmptyItemIngredient(),new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationThorium", chanceOreStack("dustBismuth", 1, 13), chanceOreStack("dustLead", 1, 33), chanceOreStack("ingotMercury", 1, 15), chanceOreStack("dustPlatinum", 1, 12), chanceOreStack("dustOsmium", 1, 15), chanceOreStack("dustHafnium", 1, 12), new EmptyItemIngredient(),new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationRadium", chanceOreStack("dustBismuth", 1, 16), chanceOreStack("dustLead", 1, 32), chanceOreStack("ingotMercury", 1, 14), chanceOreStack("dustPlatinum", 1, 11), chanceOreStack("dustOsmium", 1, 14), chanceOreStack("dustHafnium", 1, 13), new EmptyItemIngredient(),new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationPolonium", chanceOreStack("dustBismuth", 1, 16), chanceOreStack("dustLead", 1, 17), chanceOreStack("ingotMercury", 1, 16), chanceOreStack("dustOsmium", 1, 18), chanceOreStack("dustTungsten", 1, 15), chanceOreStack("dustHafnium", 1, 18), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationBismuth", chanceOreStack("dustLead", 1, 21), chanceOreStack("ingotMercury", 1, 16), chanceOreStack("dustOsmium", 1, 17), chanceOreStack("dustTungsten", 1, 14), chanceOreStack("dustHafnium", 1, 18), chanceOreStack("dustYtterbium", 1, 14), new EmptyItemIngredient(), new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationLead", chanceOreStack("ingotMercury", 1, 18), chanceOreStack("dustOsmium", 1, 18), chanceOreStack("dustTungsten", 1, 15), chanceOreStack("dustHafnium", 1, 19), chanceOreStack("dustYtterbium", 1, 15), chanceOreStack("dustGadolinium", 1, 15),new EmptyItemIngredient(),new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationMercury", chanceOreStack("dustOsmium", 1, 17), chanceOreStack("dustHafnium", 1, 19), chanceOreStack("dustYtterbium", 1, 15), chanceOreStack("dustErbium", 1, 15), chanceOreStack("dustGadolinium", 1, 17), chanceOreStack("dustSamarium", 1, 17),new EmptyItemIngredient(),new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationGold", chanceOreStack("dustPlatinum", 1, 19), chanceOreStack("dustOsmium", 1, 15), chanceOreStack("dustHafnium", 1, 17), chanceOreStack("dustErbium", 1, 14), chanceOreStack("dustGadolinium", 1, 17), chanceOreStack("dustSamarium", 1, 18),new EmptyItemIngredient(),new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationPlatinum", chanceOreStack("dustOsmium", 1, 16), chanceOreStack("dustHafnium", 1, 17), chanceOreStack("dustErbium", 1, 15), chanceOreStack("dustDysprosium", 1, 15), chanceOreStack("dustGadolinium", 1, 18), chanceOreStack("dustSamarium", 1, 19), new EmptyItemIngredient(),new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationIridium", chanceOreStack("dustOsmium", 1, 22), chanceOreStack("dustHafnium", 1, 15), chanceOreStack("dustErbium", 1, 13), chanceOreStack("dustDysprosium", 1, 14), chanceOreStack("dustGadolinium", 1, 17), chanceOreStack("dustSamarium", 1, 19), new EmptyItemIngredient(),new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationOsmium", chanceOreStack("dustHafnium", 1, 17), chanceOreStack("dustErbium", 1, 15), chanceOreStack("dustDysprosium", 1, 15), chanceOreStack("dustGadolinium", 1, 18), chanceOreStack("dustSamarium", 1, 20), chanceOreStack("dustNeodymium", 1, 15), new EmptyItemIngredient(),new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationTungsten", chanceOreStack("dustHafnium", 1, 15), chanceOreStack("dustDysprosium", 1, 14), chanceOreStack("dustGadolinium", 1, 17), chanceOreStack("dustSamarium", 1, 20), chanceOreStack("dustNeodymium", 1, 16), chanceOreStack("dustBarium", 1, 18),new EmptyItemIngredient(),new EmptyItemIngredient());
		NCBridge.addFuelReprocessor(NCRecipes.fuel_reprocessor, "wasteSpallationHafnium", chanceOreStack("dustErbium", 1, 12), chanceOreStack("dustDysprosium", 1, 13), chanceOreStack("dustGadolinium", 1, 17), chanceOreStack("dustSamarium", 1, 21), chanceOreStack("dustNeodymium", 1, 17), chanceOreStack("dustBarium", 1, 20),new EmptyItemIngredient(),new EmptyItemIngredient());


		//Collectors
		AtmosphereCollectorRecipes.registerRecipes();
		LiquidCollectorRecipes.registerRecipes();






		// Turbine
		NCBridge.add(NCRecipes.turbine, fluidStack("high_pressure_mercury", 1), fluidStack("exhaust_mercury", 3), 192D, 3D);

		// Condenser, with NCO's ratios on plain NC's scale of 1/50 of NCO's time per mB
		NCBridge.add(NCRecipes.condenser, fluidStack("exhaust_mercury", 6000), fluidStack("hot_mercury", 1000), 2560D, 700);
		NCBridge.add(NCRecipes.condenser, fluidStack("hot_mercury", 1000), fluidStack("mercury", 1000), 5120D, 700);
		NCBridge.add(NCRecipes.condenser, fluidStack("condensate_water", 1000), fluidStack("water", 1000), 640D, 350);


		// distiller
		NCBridge.addCentrifuge(NCRecipes.centrifuge, fluidStack("salt_water", 10*BUCKET_VOLUME),new EmptyFluidIngredient(),fluidStack("sodium_chloride_solution", GEM_VOLUME), fluidStack("water", BUCKET_VOLUME*9),new EmptyFluidIngredient(),new EmptyFluidIngredient(),new EmptyFluidIngredient(),new EmptyFluidIngredient(),new EmptyFluidIngredient(),new EmptyFluidIngredient(),1D,1D);
		NCBridge.addCentrifuge(NCRecipes.centrifuge, fluidStack("liquid_air", 1000),new EmptyFluidIngredient(),fluidStack("nitrogen", 38400), fluidStack("oxygen", 12800), fluidStack("argon", 6400),fluidStack("neon", 5120),fluidStack("helium", 1280),new EmptyFluidIngredient(),new EmptyFluidIngredient(),new EmptyFluidIngredient(),1D,1D);

		// Crafting
		QMDCraftingRecipeHandler.registerCraftingRecipes();

		// Furnace
		for (int i = 0; i < IngotType.values().length; i++)
		{
			String type = StringHelper.capitalize(IngotType.values()[i].getName());
			if (!ore_dict_raw_material_recipes)
			{
				GameRegistry.addSmelting(new ItemStack(QMDItems.dust, 1, i), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(QMDItems.ingot, 1, i), "ingot" + type), 0F);
			}
			else for (ItemStack dust : OreDictionary.getOres("dust" + type))
			{
				GameRegistry.addSmelting(dust, OreDictHelper.getPrioritisedCraftingStack(new ItemStack(QMDItems.ingot, 1, i), "ingot" + type), 0F);
			}
		}

		for (int i = 0; i < IngotType2.values().length; i++)
		{
			if (i == IngotType2.MERCURY.getID())
			{
				continue;
			}

			String type = StringHelper.capitalize(IngotType2.values()[i].getName());
			if (!ore_dict_raw_material_recipes)
			{
				GameRegistry.addSmelting(new ItemStack(QMDItems.dust2, 1, i), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(QMDItems.ingot2, 1, i), "ingot" + type), 0F);
			}
			else for (ItemStack dust : OreDictionary.getOres("dust" + type))
			{
				GameRegistry.addSmelting(dust, OreDictHelper.getPrioritisedCraftingStack(new ItemStack(QMDItems.ingot2, 1, i), "ingot" + type), 0F);
			}
		}

	}





	public static void reductionIsotopeRecipes(Item isotope, int noTypes)
	{
		for (int i = 0; i < noTypes; i++)
		{
			GameRegistry.addSmelting(new ItemStack(isotope, 1, 5 * i + 2), new ItemStack(isotope, 1, 5 * i), 0F);
			GameRegistry.addSmelting(new ItemStack(isotope, 1, 5 * i + 3), new ItemStack(isotope, 1, 5 * i), 0F);
		}
	}

	public static void reductionFissionFuelRecipes(Item pellet, Item fuel, int noTypes)
	{
		for (int i = 0; i < noTypes; i++)
		{
			GameRegistry.addSmelting(new ItemStack(fuel, 1, 4 * i + 1), new ItemStack(pellet, 1, 2 * i), 0F);
			GameRegistry.addSmelting(new ItemStack(fuel, 1, 4 * i + 2), new ItemStack(pellet, 1, 2 * i), 0F);
		}
	}

	public static double getDecayHastenerTimeMultipler(double radiation)
	{
		double F = Math.log1p(Math.log(2D)), Z = 0.1674477985420331D;
		return NCMath.roundTo(Z * (radiation >= 1D ? F / Math.log1p(Math.log1p(radiation)) : Math.log1p(Math.log1p(1D / radiation)) / F), 5D / NCConfig.processor_time[2]);
	}


	public static FluidIngredient fluidStack(String fluidName, int stackSize)
	{
		if (!FluidRegHelper.fluidExists(fluidName))
			return null;
		return new FluidIngredient(fluidName, stackSize);
	}

}
