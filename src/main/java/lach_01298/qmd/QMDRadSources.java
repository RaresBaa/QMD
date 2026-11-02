package lach_01298.qmd;

import lach_01298.qmd.block.QMDBlocks;
import lach_01298.qmd.enums.MaterialTypes.*;
import lach_01298.qmd.item.QMDItems;
import nc.radiation.RadSources;
import net.minecraft.item.ItemStack;

public class QMDRadSources
{
	
	public static final double FLUID_MULTIPLIER = 125D / 18D;
	// In NCO's RadSources but not in plain NC's
	public static final double RADIUM = 0.000625;
	public static final double STRONTIUM_90 = 0.0345D;
	public static final double RUTHENIUM_106 = 0.98D;
	public static final double EUROPIUM_155 = 0.21D;
	public static final double SODIUM_22 = 0.384;
	public static final double BERYLLIUM_7 = 6.89;
	public static final double URANIUM_234 = 0.00000407;
	public static final double PROTACTINIUM_231 = 0.0000305;
	public static final double COBALT_60 = 0.190;
	public static final double IRIDIUM_192 = 4.94;
	public static final double COPERNICIUM_291 = 0.000833;
	public static final double MIX_291 = COPERNICIUM_291;
	
	
	public static void init()
	{
		RadSources.putMaterial(BERYLLIUM_7, "Beryllium7");
		RadSources.putMaterial(SODIUM_22, "Sodium22");
		RadSources.putMaterial(URANIUM_234, "Uranium234");
		RadSources.putMaterial(PROTACTINIUM_231, "Protactinium231");
		RadSources.putMaterial(COBALT_60, "Cobalt60");
		RadSources.putMaterial(IRIDIUM_192, "Iridium192");
		
		RadSources.put(SODIUM_22/2D, new ItemStack(QMDItems.source,1,SourceType.SODIUM_22.getID()));
		RadSources.put(COBALT_60/2D, new ItemStack(QMDItems.source,1,SourceType.COBALT_60.getID()));
		RadSources.put(RadSources.PLUTONIUM_239/2D, new ItemStack(QMDItems.source,1,SourceType.PLUTONIUM_BERYLLIUM.getID()));
		RadSources.put(IRIDIUM_192/2D, new ItemStack(QMDItems.source,1,SourceType.IRIDIUM_192.getID()));
		
		
		RadSources.putFluid(RadSources.TRITIUM, "antitritium");
		
		RadSources.put(RadSources.CAESIUM_137, "wasteFissionHeavy");
		RadSources.put(RadSources.CAESIUM_137, "wasteFissionLight");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationCalifornium");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationBerkelium");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationCurium");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationAmericium");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationPlutonium");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationNeptunium");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationUranium");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationProtactinium");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationThorium");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationRadium");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationPolonium");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationBismuth");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationLead");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationGold");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationPlatinum");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationIridium");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationOsmium");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationTungsten");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationHafnium");
		RadSources.put(RadSources.CAESIUM_137/2d, "wasteSpallationMercury");
		
		RadSources.put(RadSources.TRITIUM*FLUID_MULTIPLIER*0.1, "cellAntitritium");
		
		RadSources.put(RADIUM/16D, new ItemStack(QMDItems.luminousPaint,1,LuminousPaintType.GREEN.getID()));
		RadSources.put(RADIUM/16D, new ItemStack(QMDItems.luminousPaint,1,LuminousPaintType.BLUE.getID()));
		RadSources.put(RADIUM/16D, new ItemStack(QMDItems.luminousPaint,1,LuminousPaintType.ORANGE.getID()));
		RadSources.put(RADIUM/16D, QMDBlocks.greenLuminousPaint);
		RadSources.put(RADIUM/16D, QMDBlocks.blueLuminousPaint);
		RadSources.put(RADIUM/16D, QMDBlocks.orangeLuminousPaint);
		
		
		RadSources.putIsotope(COPERNICIUM_291, "Copernicium291", "copernicium_291");
		
		RadSources.addToFoodMaps(new ItemStack(QMDItems.flesh), 0, 3.0);
	}
	
	
	
}
