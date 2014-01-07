package be.pirlewiet.registrations.model;

import java.util.List;

public class InschrijvingenForm {
	private List<Deelnemer> participants;
	private AanvraagInschrijving aanvraagInschrijving;

	public List<Deelnemer> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Deelnemer> participants) {
		this.participants = participants;
	}

	public AanvraagInschrijving getAanvraagInschrijving() {
		return aanvraagInschrijving;
	}

	public void setAanvraagInschrijving(AanvraagInschrijving aanvraagInschrijving) {
		this.aanvraagInschrijving = aanvraagInschrijving;
	}
}
