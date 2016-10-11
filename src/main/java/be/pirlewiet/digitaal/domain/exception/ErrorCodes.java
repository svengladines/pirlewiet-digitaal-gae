package be.pirlewiet.digitaal.domain.exception;

import be.occam.utils.spring.web.ErrorCode;

public class ErrorCodes {
	 
	public static ErrorCode PWT_UNKNOWN_ACTOR = new ErrorCode("pwtUnknownActor");
	
	public static ErrorCode APPLICATION_NOT_FOUND = new ErrorCode("APPLICATION_NOT_FOUND");
	public static ErrorCode APPLICATION_HOLIDAY_NONE = new ErrorCode("APPLICATION_HOLIDAY_NONE");
	public static ErrorCode APPLICATION_HOLIDAY_MIXED = new ErrorCode("APPLICATION_HOLIDAY_MIXED");
	public static ErrorCode APPLICATION_HOLIDAY_NOT_FOUND = new ErrorCode("APPLICATION_HOLIDAY_NOT_FOUND");
	public static ErrorCode APPLICATION_CONTACT_INCOMPLETE = new ErrorCode("APPLICATION_CONTACT_INCOMPLETE");
	public static ErrorCode APPLICATION_QLIST_INCOMPLETE = new ErrorCode("APPLICATION_QLIST_INCOMPLETE");
	public static ErrorCode APPLICATION_NO_ENROLLMENTS = new ErrorCode("APPLICATION_NO_ENROLLMENTS");
	
	public static ErrorCode PARTICIPANT_DATA_GIVEN_NAME_MISSING = new ErrorCode("PARTICIPANT_DATA_GIVEN_NAME_MISSING");
	public static ErrorCode PARTICIPANT_DATA_FAMILY_NAME_MISSING = new ErrorCode("PARTICIPANT_DATA_FAMILY_NAME_MISSING");
	public static ErrorCode PARTICIPANT_DATA_BIRTHDAY_MISSING = new ErrorCode("PARTICIPANT_DATA_BIRTHDAY_MISSING");
	public static ErrorCode PARTICIPANT_DATA_GENDER_MISSING = new ErrorCode("PARTICIPANT_DATA_GENDER_MISSING");
	public static ErrorCode PARTICIPANT_DATA_PHONE_MISSING = new ErrorCode("PARTICIPANT_DATA_PHONE_MISSING");
	public static ErrorCode PARTICIPANT_MEDIC_QUESTION_MISSING = new ErrorCode("PARTICIPANT_MEDIC_QUESTION_MISSING");
	
	public static ErrorCode ORGANISATION_NAME_MISSING = new ErrorCode("ORGANISATION_NAME_MISSING");
	public static ErrorCode ORGANISATION_EMAIL_MISSING = new ErrorCode("ORGANISATION_EMAIL_MISSING");
	public static ErrorCode ORGANISATION_PHONE_MISSING = new ErrorCode("ORGANISATION_PHONE_MISSING");
	public static ErrorCode ORGANISATION_NOT_FOUND = new ErrorCode("ORGANISATION_NOT_FOUND");
	public static ErrorCode ORGANISATION_EMAIL_TAKEN = new ErrorCode("ORGANISATION_EMAIL_TAKEN");
	
	public static ErrorCode ADDRESS_CITY_MISSING = new ErrorCode("ADDRESS_CITY_MISSING");
	public static ErrorCode ADDRESS_STREET_MISSING = new ErrorCode("ADDRESS_STREET_MISSING");
	public static ErrorCode ADDRESS_NUMBER_MISSING = new ErrorCode("ADDRESS_NUMBER_MISSING");
	public static ErrorCode ADDRESS_ZIPCODE_MISSING = new ErrorCode("ADDRESS_ZIPCODE_MISSING");

}
