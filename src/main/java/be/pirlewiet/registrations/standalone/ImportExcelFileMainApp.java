package be.pirlewiet.registrations.standalone;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ImportExcelFileMainApp {
	private final static Logger LOGGER = LoggerFactory.getLogger(ImportExcelFileMainApp.class);
	private static String lijn = "---------------------------------------------------";

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");

		String filename = "";

		if (args.length <= 0) {
			LOGGER.info("Error! Geef het pad naar de Excel-file als parameter!");
			return;
		}

		filename = args[0];

		ImportSpreadSheetv2 iss = (ImportSpreadSheetv2) ctx.getBean("importSpreadSheetv2");
		LOGGER.info(lijn);
		LOGGER.info("Starting import Excel file:");
		LOGGER.info(lijn);
		LOGGER.info("ImportSpreadSheet instance    ==== " + iss);
		LOGGER.info("Pad naar de import-Excel file ==== " + filename);
		LOGGER.info(lijn);
		LOGGER.info("STARTING IMPORT");
		LOGGER.info(lijn);
		try {
			iss.ImportSpreadSheet(filename);
		} catch (UnsupportedOperationException e) {
			LOGGER.info(lijn);
			LOGGER.info("**************ERROR**************");
			LOGGER.info(lijn);
			LOGGER.info("An unsupported operation exception has occurred, please contact your application administrator, or try again.");
			return;
		} catch (IOException e) {
			LOGGER.info(lijn);
			LOGGER.info("**************ERROR**************");
			LOGGER.info(lijn);
			LOGGER.info("An I/O exception has occurred, please contact your application administrator, or try again.");
			return;
		}

		LOGGER.info(lijn);
		LOGGER.info("Import succeeded!!!!");
	}
}
