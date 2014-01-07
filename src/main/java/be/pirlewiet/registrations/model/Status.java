package be.pirlewiet.registrations.model;

public enum Status {
    NIEUW(false,"Nieuw"),
    INBEHANDELING(true,"In behandeling"),
    HUISBEZOEK(false,"Huisbezoek gepland"),
    WACHTLIJST(false,"Wachtlijst"),
    WEIGERINGLEEFTIJD(false,"Geweigerd (Maximum leeftijd voor kamp overschreden)"),
    WEIGERINGMAXAANTALKAMPEN(false,"Geweigerd (Deelnemer heeft het maximum aantal kampen overschreden)"),
    WEIGERINGZWARTELIJST(false,"Geweigerd"),
    WEIGERING(false,"Geweigerd"),
    GEANNULEERDDEELNEMER(false,"Geannuleerd door deelnemer"),
    GEANNULEERDPIRLEWIET(false,"Geannuleerd door Pirlewiet VZW"),
    BEVESTIGD(true,"Bevestigd");

    private final boolean going;
    private final String displayString;

    Status(){
        going = false;
        displayString = "";
    }
    Status(boolean going,String displayString){
        this.going = going;
        this.displayString = displayString;
    }
    public boolean isGoing(){
        return going;
    }

    public String getDisplayString() {
		return displayString;
	}
	@Override
    public String toString() {
    	return displayString;
    }
}
