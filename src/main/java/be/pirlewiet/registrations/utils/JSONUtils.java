/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.pirlewiet.registrations.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.model.DienstJsonObject;

/**
 * 
 * @author bgri978
 */
@Component
public class JSONUtils {

	public List<DienstJsonObject> generateDienstJsonList(List<Dienst> listDienst) {
		List<DienstJsonObject> dienstJsonList = new ArrayList<DienstJsonObject>();

		for (Dienst d : listDienst) {
			DienstJsonObject tempObject = new DienstJsonObject();
			tempObject.setId(d.getId());
			tempObject.setNaam(d.getNaam());
			tempObject.setEmail(d.getEmailadres());
			tempObject.setFax(d.getFaxnummer());
			tempObject.setGemeente(d.getAdres().getGemeente());
			tempObject.setNummer(d.getAdres().getNummer());
			tempObject.setPostcode(d.getAdres().getPostcode());
			tempObject.setStraat(d.getAdres().getStraat());
			tempObject.setTelefoon(d.getTelefoonnummer());
			tempObject.setAfdeling(d.getAfdeling());
			tempObject.setActief(d.getCredentials().isEnabled());
			dienstJsonList.add(tempObject);
		}

		return dienstJsonList;
	}
}
