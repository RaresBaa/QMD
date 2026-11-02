package lach_01298.qmd.nco.config;

/**
 * Defaults from NCO's config, for bundled code reading options that plain NC's config doesn't have.
 */
public class NCOConfig {
	
	public static final double processor_time_multiplier = 1D;
	public static final double processor_power_multiplier = 1D;
	public static final boolean factor_recipes = false;
	public static final int[] upgrade_stack_sizes = {64, 64};
	public static final boolean[] enable_fluid_recipe_expansion = {true, true};
	
	public static final double[] heat_exchanger_heat_transfer_coefficient = {16D, 24D, 32D};
	public static final double[] heat_exchanger_heat_retention_mult = {0.9D, 0.95D, 1D};
	public static final boolean heat_exchanger_lmtd = false;
	
	public static final double fission_fuel_time_multiplier = 1D;
	public static final double fission_fuel_heat_multiplier = 1D;
	public static final double fission_fuel_efficiency_multiplier = 1D;
	public static final double fission_fuel_radiation_multiplier = 1D;
	public static final double fusion_fuel_time_multiplier = 1D;
	public static final double fusion_fuel_heat_multiplier = 1D;
	public static final double fusion_fuel_radiation_multiplier = 1D;
	public static final double turbine_spin_up_multiplier_global = 1D;
	public static final int quantum_angle_precision = 16;
	public static final boolean radiation_scrubber_non_linear = true;
}
