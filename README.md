# QMD
A Nuclearcraft addon that aims to bring particle accelerators and particle physics to minecraft, ported from NuclearCraft Overhauled to plain NuclearCraft 2.19c.

## How the port works

- `lach_01298.qmd.nco` holds the NCO classes QMD needs, renamed so they can't clash with plain NC's `nc.*`.
  `nco.legacy` recreates the `nclegacy` classes from NCO's history, since that library publishes no source.
- `recipes/NCBridge.java` passes QMD's recipes to plain NC. Assembler recipes become shapeless crafting, the enricher maps to the dissolver,
  the separator to the isotope separator, and centrifuge and fuel reprocessor recipes keep only as many outputs as plain NC's machines have.
- QMD now provides the multitool and the materials and fluids plain NC lacks.
- NCO's fission irradiator recipes moved to QMD's irradiator. It needs a source to run, so a new Plutonium-Beryllium Source, crafted from Pu-239, lets a
  player make their first N-doped silicon and Co-60 without an accelerator.
- `tools/port` holds the one-off scripts that made the port.

Removed because plain NC has no counterpart: fission reflectors and shield, strontium RTG, super alloy turbine blade, copernicium fission
fuels, recipes for NCO's decay pool and heat exchanger, mercury topping cycles (plain NC's fission reactors can't heat fluids), and CraftTweaker
registration of custom QMD blocks.

`override_nc_recipes` defaults to false here, so QMD keeps plain NC's graphite, coal and supercooler recipes, which a pack may rely on. Graphite melts
into coal and supercoolers need 320 mB of gas per mB of liquid, as in plain NC. QMD's carbon fluid comes from charcoal and centrifuged coal.

The Liquefier is built from QMD's own `qmd:liquefier_*` casing, glass, baffle, inlet, outlet and tube blocks, because plain NC's heat exchanger
blocks use a different multiblock system. The mercury loop's turbine and condenser recipes run on plain NC's multiblocks, with NCO's ratios and
plain NC's time scale.