package lach_01298.qmd.recipes;

import lach_01298.qmd.QMD;
import lach_01298.qmd.util.Util;
import lach_01298.qmd.nco.recipe.ingredient.*;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Hands QMD's recipes to plain NuclearCraft, whose machines and ingredient classes differ from the Overhauled ones QMD was written for.
 */
public final class NCBridge
{
	private NCBridge() {}
	
	/**
	 * Converts a bundled NCO ingredient to the matching plain NC one. Anything else, such as a String or ItemStack, passes through.
	 */
	public static Object convert(Object o)
	{
		if (o instanceof EmptyItemIngredient) return new nc.recipe.ingredient.EmptyItemIngredient();
		if (o instanceof EmptyFluidIngredient) return new nc.recipe.ingredient.EmptyFluidIngredient();
		if (o instanceof OreIngredient ore) return new nc.recipe.ingredient.OreIngredient(ore.oreName, ore.stackSize);
		if (o instanceof ItemIngredient item) return new nc.recipe.ingredient.ItemIngredient(item.stack);
		if (o instanceof FluidIngredient fluid) return new nc.recipe.ingredient.FluidIngredient(fluid.fluidName, fluid.amount);
		if (o instanceof ChanceItemIngredient chance)
		{
			return new nc.recipe.ingredient.ChanceItemIngredient((nc.recipe.ingredient.IItemIngredient) convert(chance.ingredient), chance.chancePercent, chance.minStackSize);
		}
		if (o instanceof ChanceFluidIngredient chance)
		{
			return new nc.recipe.ingredient.ChanceFluidIngredient((nc.recipe.ingredient.IFluidIngredient) convert(chance.ingredient), chance.chancePercent, chance.stackDiff, chance.minStackSize);
		}
		if (o instanceof ItemArrayIngredient array)
		{
			List<nc.recipe.ingredient.IItemIngredient> list = new ArrayList<>();
			array.ingredientList.forEach(i -> list.add((nc.recipe.ingredient.IItemIngredient) convert(i)));
			return new nc.recipe.ingredient.ItemArrayIngredient(list);
		}
		if (o instanceof FluidArrayIngredient array)
		{
			List<nc.recipe.ingredient.IFluidIngredient> list = new ArrayList<>();
			array.ingredientList.forEach(i -> list.add((nc.recipe.ingredient.IFluidIngredient) convert(i)));
			return new nc.recipe.ingredient.FluidArrayIngredient(list);
		}
		if (o instanceof List<?> list)
		{
			List<Object> out = new ArrayList<>();
			list.forEach(i -> out.add(convert(i)));
			return out;
		}
		return o;
	}
	
	private static Object[] convertAll(Object... args)
	{
		Object[] out = new Object[args.length];
		for (int i = 0; i < args.length; i++)
		{
			out[i] = convert(args[i]);
		}
		return out;
	}
	
	/** NCO's stack helpers return null for an ore or fluid that doesn't exist, which is how a material missing from plain NC shows up. */
	private static boolean hasMissing(String machine, Object... args)
	{
		for (Object arg : args)
		{
			if (arg == null)
			{
				Util.getLogger().warn("Skipping " + machine + " recipe with a missing material: " + java.util.Arrays.toString(args));
				return true;
			}
		}
		return false;
	}
	
	/** For a machine whose slots match between NC and NCO. */
	public static void add(ProcessorRecipeHandler handler, Object... args)
	{
		if (hasMissing(handler.getRecipeName(), args)) return;
		handler.addRecipe(convertAll(args));
	}
	
	/** Fuel reprocessing has 8 outputs in NCO and 4 in NC: keep the 4 likeliest. */
	public static void addFuelReprocessor(ProcessorRecipeHandler handler, Object input, Object... rest)
	{
		if (input == null)
		{
			hasMissing(handler.getRecipeName(), input);
			return;
		}
		for (int i = 0; i < 8; i++)
		{
			if (rest[i] == null) rest[i] = new EmptyItemIngredient();
		}
		List<Object> outputs = new ArrayList<>();
		for (int i = 0; i < 8; i++)
		{
			if (!(rest[i] instanceof EmptyItemIngredient)) outputs.add(rest[i]);
		}
		outputs.sort(Comparator.comparingInt(o -> o instanceof ChanceItemIngredient c ? -c.chancePercent : -100));
		List<Object> args = new ArrayList<>();
		args.add(input);
		for (int i = 0; i < 4; i++)
		{
			args.add(i < outputs.size() ? outputs.get(i) : new EmptyItemIngredient());
		}
		for (int i = 8; i < rest.length; i++) args.add(rest[i]);
		add(handler, args.toArray());
	}
	
	/** Centrifuging has 6 fluid outputs in NCO and 4 in NC: keep the first 4. */
	public static void addCentrifuge(ProcessorRecipeHandler handler, Object input, Object... rest)
	{
		if (hasMissing(handler.getRecipeName(), input) || hasMissing(handler.getRecipeName(), rest)) return;
		List<Object> args = new ArrayList<>();
		args.add(input);
		for (int i = 0; i < 4; i++) args.add(rest[i]);
		for (int i = 6; i < rest.length; i++) args.add(rest[i]);
		add(handler, args.toArray());
	}
	
	public static void remove(ProcessorRecipeHandler handler, List<IItemIngredient> items, List<IFluidIngredient> fluids)
	{
		if (items.contains(null) || fluids.contains(null)) return;
		List<nc.recipe.ingredient.IItemIngredient> plainItems = new ArrayList<>();
		items.forEach(i -> plainItems.add((nc.recipe.ingredient.IItemIngredient) convert(i)));
		List<nc.recipe.ingredient.IFluidIngredient> plainFluids = new ArrayList<>();
		fluids.forEach(i -> plainFluids.add((nc.recipe.ingredient.IFluidIngredient) convert(i)));
		ProcessorRecipe recipe = handler.getRecipeFromIngredients(plainItems, plainFluids);
		if (recipe != null) handler.removeRecipe(recipe);
	}
	
	private static int assemblerCount = 0;
	
	/**
	 * NC has no assembler, so its recipes become shapeless crafting. All of them fit a 3x3 grid. Arguments are NCO's: four inputs,
	 * the output, then the time and power multipliers, which crafting has no use for.
	 */
	public static void addAssembler(Object in1, Object in2, Object in3, Object in4, Object output, Object... ignored)
	{
		if (hasMissing("assembler", in1, in2, in3, in4, output)) return;
		ItemStack result = resolveOutput(output);
		if (result.isEmpty())
		{
			Util.getLogger().warn("No item for assembler output " + output + ", skipping its crafting recipe");
			return;
		}
		List<Object> inputs = new ArrayList<>();
		for (Object in : new Object[] {in1, in2, in3, in4})
		{
			addInput(inputs, in);
		}
		ResourceLocation name = new ResourceLocation(QMD.MOD_ID, "assembler_" + assemblerCount++);
		ForgeRegistries.RECIPES.register(new ShapelessOreRecipe(name, result, inputs.toArray()).setRegistryName(name));
	}
	
	private static void addInput(List<Object> inputs, Object in)
	{
		if (in instanceof EmptyItemIngredient) return;
		if (in instanceof String ore) inputs.add(ore);
		else if (in instanceof ItemStack stack)
		{
			for (int i = 0; i < stack.getCount(); i++) inputs.add(stack.copy().splitStack(1));
		}
		else if (in instanceof OreIngredient ore)
		{
			for (int i = 0; i < ore.stackSize; i++) inputs.add(ore.oreName);
		}
		else if (in instanceof List<?> options)
		{
			List<ItemStack> stacks = new ArrayList<>();
			int count = 1;
			for (Object option : options)
			{
				if (option instanceof OreIngredient ore)
				{
					stacks.addAll(OreDictionary.getOres(ore.oreName));
					count = ore.stackSize;
				}
			}
			Ingredient ingredient = Ingredient.fromStacks(stacks.toArray(new ItemStack[0]));
			for (int i = 0; i < count; i++) inputs.add(ingredient);
		}
		else throw new IllegalArgumentException("Unexpected assembler input: " + in);
	}
	
	private static ItemStack resolveOutput(Object output)
	{
		String ore = output instanceof String s ? s : output instanceof OreIngredient o ? o.oreName : null;
		int count = output instanceof OreIngredient o ? o.stackSize : 1;
		if (ore != null)
		{
			List<ItemStack> ores = OreDictionary.getOres(ore);
			if (ores.isEmpty()) return ItemStack.EMPTY;
			ItemStack stack = ores.get(0).copy();
			stack.setCount(count);
			return stack;
		}
		return output instanceof ItemStack stack ? stack : ItemStack.EMPTY;
	}
}
