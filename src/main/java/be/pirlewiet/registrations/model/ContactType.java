package be.pirlewiet.registrations.model;

public enum ContactType {
    Dienst("dienst"),
    Gezin("gezin"),
    GezinDienst("gezin + dienst");

    private String displayName;

	private ContactType(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return displayName;
	}

	public String getDisplayName() {
		return displayName;
	}


}
