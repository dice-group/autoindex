package org.aksw.simba.Initializer;

public class EnergyFactory {
	public EnergyFunction getEnergyFunction(String EnergyType) {
		if (EnergyType.equals("LevDist")) {
			return new LevDist();
		} else
			return null;
	}
}
