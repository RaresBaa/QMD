package lach_01298.qmd.nco.enumm;

public interface IFissionFuelEnum extends IMetaEnum {
	
	int getBaseTime();
	
	int getBaseHeat();
	
	double getBaseEfficiency();
	
	int getCriticality();
	
	boolean getSelfPriming();
}
