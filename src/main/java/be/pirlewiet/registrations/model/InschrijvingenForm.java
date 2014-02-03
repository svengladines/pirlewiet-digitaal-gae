package be.pirlewiet.registrations.model;

import java.util.List;

public class InschrijvingenForm {
	private List<Deelnemer> participants;
	private Inschrijving aanvraagInschrijving;

	public List<Deelnemer> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Deelnemer> participants) {
		this.participants = participants;
	}

	public Inschrijving getAanvraagInschrijving() {
		return aanvraagInschrijving;
	}

	public void setAanvraagInschrijving(Inschrijving aanvraagInschrijving) {
		this.aanvraagInschrijving = aanvraagInschrijving;
	}
}
