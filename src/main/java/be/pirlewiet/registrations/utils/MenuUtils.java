package be.pirlewiet.registrations.utils;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import be.pirlewiet.registrations.view.controllers.ScreenDropDownObject;

@Component
public class MenuUtils {

	public static final String DOORVERWIJZER = "Doorverwijzer";
	public static final String VAKANTIEPROJECTEN = "Vakantieprojecten";
	public static final String INSCHRIJVINGEN = "Inschrijvingen";
	public static final String DEELNEMERS = "Deelnemers";

	/**
	 * Creates a dropDownObject with a list for the select screen drop down menu
	 * 
	 * @return
	 */
	public ScreenDropDownObject createDropDownObject() {
		ScreenDropDownObject dropDownObject = new ScreenDropDownObject();

		ArrayList<String> screenList = new ArrayList<String>();
		screenList.add(DEELNEMERS);
		screenList.add(DOORVERWIJZER);
		screenList.add(VAKANTIEPROJECTEN);
		screenList.add(INSCHRIJVINGEN);

		dropDownObject.setScreens(screenList);

		return dropDownObject;
	}

}
