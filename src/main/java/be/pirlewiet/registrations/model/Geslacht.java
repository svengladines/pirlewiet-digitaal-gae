package be.pirlewiet.registrations.model;

public enum Geslacht {
	Onbekend("-"),Vrouw("v"),Man("m");
	
	private String indicator;
	
	private Geslacht(String indicator) {
		this.indicator = indicator;
	}
	public String getIndicator() {
		return this.indicator;
	}
	
	@Override
	public String toString() {
		return indicator;
	}
	
}
