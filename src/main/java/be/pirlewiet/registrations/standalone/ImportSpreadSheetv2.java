/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.pirlewiet.registrations.standalone;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import jxl.Cell;
import jxl.CellFeatures;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.ldap.common.util.EqualsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import be.pirlewiet.registrations.model.AanvraagInschrijving;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.ContactType;
import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.model.Geslacht;
import be.pirlewiet.registrations.model.Status;
import be.pirlewiet.registrations.model.VakantieProject;
import be.pirlewiet.registrations.model.Vakantietype;
import be.pirlewiet.registrations.services.ContactpersoonService;
import be.pirlewiet.registrations.services.DeelnemerService;
import be.pirlewiet.registrations.services.DienstService;
import be.pirlewiet.registrations.services.InschrijvingService;
import be.pirlewiet.registrations.services.VakantieProjectService;
import be.pirlewiet.registrations.services.VakantietypeService;
import be.pirlewiet.registrations.utils.DateUtils;

@Component
public class ImportSpreadSheetv2 {
	private final static Logger LOGGER = LoggerFactory.getLogger(ImportSpreadSheetv2.class);
	private final static int COLNO_emptycol = 50;
	private final static int COLNO_gezinnr = 0;
	private final static int COLNO_indiv_nr = COLNO_emptycol;
	private final static int COLNO_deelnemer_voornaam = 1;
	private final static int COLNO_deelnemer_famnaam = 2;
	private final static int COLNO_deelnemer_mv = 3;
	private final static int COLNO_deelnemer_gebdate = 4;
	private final static int COLNO_dienst_naam = 5;
	private final static int COLNO_dienst_straatnr = 6;
	private final static int COLNO_dienst_postcode = 7;
	private final static int COLNO_dienst_gemeenete = 8;
	private final static int COLNO_dienst_tel = 9;
	private final static int COLNO_dienst_email = 10;
	// private final static int COLNO_dienst_fax = COLNO_emptycol;
	private final static int COLNO_dienst_contactpers = 11;
	private final static int COLNO_aanvraag_adres_straatnr = 12;
	private final static int COLNO_aanvraag_adres_postcode = 13;
	private final static int COLNO_aanvraag_adres_gemeente = 14;
	private final static int COLNO_deelnemer_tel = 15;
	private final static int COLNO_kamp2010 = 28;
	private final static int COLNO_kamp2010_inschrNr = COLNO_emptycol;
	private final static int COLNO_kamp2010_II = COLNO_emptycol;
	private final static int COLNO_kamp2010_II_inschrNr = COLNO_emptycol;
	private final static int COLNO_aanvraag_datumInschr = COLNO_emptycol;
	private final static int COLNO_aanvraag_contactType = 16;
	private final static int COLNO_2012_status = 19;
	private final static int COLNO_teBetalen = 20;
	private final static int COLNO_nogteBetalen = 22;
	private final static int COLNO_kamp2010_III = COLNO_emptycol;
	private final static int COLNO_2008_3D = COLNO_emptycol;
	private final static int COLNO_2009 = 29;
	private final static int COLNO_2008 = 30;
	private final static int COLNO_2007 = 31;
	private final static int COLNO_2006 = 32;
	private final static int COLNO_2005 = 33;
	private final static int COLNO_2004 = 34;
	private final static int COLNO_opm = 35;
	private final static int COLNO_medischeFiche = 21;
	private final static int COLNO_2012heen = 23;
	private final static int COLNO_2012terug = 24;
	private final static int COLNO_kampboekje = 25;
	private final static int COLNO_2012 = 17;
	private final static int COLNO_2012_II = 18;
	private final static int COLNO_2011 = 26;
	private final static int COLNO_2011_te_betalen = 27;
	@Autowired
	private DienstService dienstService;
	@Autowired
	private DeelnemerService deelnemerService;
	@Autowired
	private InschrijvingService inschrijvingService;
	@Autowired
	private VakantieProjectService vakantieProjectService;
	@Autowired
	private ContactpersoonService contactpersoonService;
	@Autowired
	private VakantietypeService vakantietypeService;
	@Autowired
	private DateUtils dateUtils;

	// Gekende types voor import historiek Pirlewiet inschrijvingen
	private static Vakantietype GEZINSVAKANTIE;
	private static Vakantietype KINDERKAMP;
	private static Vakantietype PAASKINDERKAMP;
	private static Vakantietype PAASGEZINSVAKANTIE;
	private static Vakantietype TIENERKAMP;
	private static Vakantietype VOLWASSENVAKANTIE;
	private static Vakantietype DRIEDAAGSE;
	private static Vakantietype BEGELEIDWONENBRUSSEL;
	private static Vakantietype DUMMYVAKANTIE;

	public ImportSpreadSheetv2() {
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void ImportSpreadSheet(String fileToImport) throws IOException, UnsupportedOperationException {
		fillStaticVakatieTypes();
		if (fileToImport == null || fileToImport.isEmpty()) {
			throw new IllegalStateException("No file to import");
		}

		File inputWorkbook = new File(fileToImport);
		Workbook w;
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(0);

			checkRowZero(sheet);
			for (int rownum = 1; rownum < sheet.getRows(); rownum++) {
				if (isEmptyRow(rownum, sheet)) {
					continue;
				}
				Contactpersoon c = null;
				// Dienst d = null;
				c = constructContactpersoonDienstFromRow(rownum, sheet);
				Deelnemer dr = constructDeelnemerFromRow(rownum, sheet);
				ContactType ct = constructContactTypeFromRow(rownum, sheet);
				String gezinsnummer = sheet.getCell(COLNO_gezinnr, rownum).getContents();
				Status statusoude = Status.BEVESTIGD;
				Status status2012_A = constructStatus2012FromRow(rownum, sheet);
				Status status2012_B = constructStatus2012FromRow(rownum, sheet);
				Date insdate = dateUtils.toDate("01/01/2000");

				Adres a = constructAdresDeelnemerFromRow(rownum, sheet);

				VakantieProject[] vps;
				AanvraagInschrijving ai;
				vps = findCreateVakantieProject(2004, sheet.getCell(COLNO_2004, rownum), rownum);
				ai = processOneVParray(vps, statusoude, c, a, dr, gezinsnummer, insdate, ct);

				vps = findCreateVakantieProject(2005, sheet.getCell(COLNO_2005, rownum), rownum);
				ai = processOneVParray(vps, statusoude, c, a, dr, gezinsnummer, insdate, ct);

				vps = findCreateVakantieProject(2006, sheet.getCell(COLNO_2006, rownum), rownum);
				ai = processOneVParray(vps, statusoude, c, a, dr, gezinsnummer, insdate, ct);

				vps = findCreateVakantieProject(2007, sheet.getCell(COLNO_2007, rownum), rownum);
				ai = processOneVParray(vps, statusoude, c, a, dr, gezinsnummer, insdate, ct);

				vps = findCreateVakantieProject(2008, sheet.getCell(COLNO_2008, rownum), rownum);
				ai = processOneVParray(vps, statusoude, c, a, dr, gezinsnummer, insdate, ct);

				vps = findCreateVakantieProject(2009, sheet.getCell(COLNO_2009, rownum), rownum);
				ai = processOneVParray(vps, statusoude, c, a, dr, gezinsnummer, insdate, ct);

				vps = findCreateVakantieProject(2010, sheet.getCell(COLNO_kamp2010, rownum), rownum);
				ai = processOneVParray(vps, statusoude, c, a, dr, gezinsnummer, insdate, ct);

				vps = findCreateVakantieProject(2011, sheet.getCell(COLNO_2011, rownum), rownum);
				ai = processOneVParray(vps, statusoude, c, a, dr, gezinsnummer, insdate, ct);

				vps = findCreateVakantieProject(2012, sheet.getCell(COLNO_2012, rownum), rownum, true, false);
				ai = processOneVParray(vps, status2012_A, c, a, dr, gezinsnummer, insdate, ct);

				vps = findCreateVakantieProject(2012, sheet.getCell(COLNO_2012_II, rownum), rownum, false, true);
				ai = processOneVParray(vps, status2012_B, c, a, dr, gezinsnummer, insdate, ct);

			}
		} catch (BiffException e) {
			throw new UnsupportedOperationException("Input spreadsheet does not conform to expected format.");
		}
	}

	public AanvraagInschrijving processOneVParray(VakantieProject[] vps, Status s, Contactpersoon c, Adres a, Deelnemer dr, String gezinsnummer, Date insdate, ContactType ct) {
		AanvraagInschrijving ai = null;
		if (vps != null) {
			for (VakantieProject vp : vps) {
				ai = findCreateAanvraagInschrijving(vp, c, a, dr, gezinsnummer, ct, s, "", insdate);
			}
		}
		return ai;
	}

	public AanvraagInschrijving findCreateAanvraagInschrijving(VakantieProject vp, Contactpersoon c, Adres a, Deelnemer d, String gezinsnummer, ContactType contacttype, Status s, String opmerkingen,
			Date datumInschrijving) {
		AanvraagInschrijving ai = null;

		// find aanvraaginschrijving with vakproj, contactpers, adres. if it
		// does not exist : create it.
		List<AanvraagInschrijving> lai = inschrijvingService.findInschrijvingByContactVakprojAdrGezinsnrConttypeOpmDatuminschr(c, vp, a, gezinsnummer, contacttype, opmerkingen, datumInschrijving);
		if (lai.isEmpty()) {
			ai = new AanvraagInschrijving();
			ai.setContactType(contacttype);
			ai.setContactpersoon(c);
			ai.addDeelnemer(d);
			ai.setDeelnemersAdres(a);
			ai.setGezinsnummer(gezinsnummer);
			ai.setInschrijvingsdatum(datumInschrijving);
			ai.setOpmerkingen(opmerkingen);
			ai.setStatus(s);
			ai.setVakantieproject(vp);
			ai = inschrijvingService.createInschrijving(ai);
		} else if (lai.size() == 1) {
			ai = lai.get(0);
			ai.addDeelnemer(d);
			ai = inschrijvingService.updateInschrijving(ai);
		} else {
			throw new RuntimeException("Problem !!!!!!!!!!!!!!!!!!!!!");
		}

		return ai;
	}

	public VakantieProject[] findCreateVakantieProject(int jaar, Cell cellVakType, int line) {
		return findCreateVakantieProject(jaar, cellVakType, line, false, false);
	}

	public VakantieProject[] findCreateVakantieProject(int jaar, Cell cellVakType, int line, boolean _2012A, boolean _2012B) {

		VakantieProject[] vp = new VakantieProject[3];
		vp[0] = null;
		vp[1] = null;
		vp[2] = null;
		Vakantietype vt = null;
		int rank = -1; // geeft aan over welke instantie vn dit type kamp dt jaa
						// gaat
		Vakantietype vt2 = null;
		int rank2 = -1;
		String code = cellVakType.getContents();

		boolean specialcase1 = (_2012A && code.equalsIgnoreCase("PK 2/K3"));
		boolean specialcase2 = (_2012B && code.equalsIgnoreCase("PK 2/K3"));

		if (!notNullnotEmpty(code) || code.equalsIgnoreCase("                                                                                     ") || code.trim().equalsIgnoreCase("wachtlijst")
				|| code.equalsIgnoreCase("eigen gemeente") || code.equalsIgnoreCase("afgevallen ") || code.startsWith("valt af ") || code.startsWith("annul ")
				|| code.startsWith("was ingeschreven maar") || code.startsWith("wachtlijst ") || code.equalsIgnoreCase("?") || code.equalsIgnoreCase("franstalig")
				|| code.equalsIgnoreCase("X: wordt sterk betwijfeld…") || code.equalsIgnoreCase("te oud")) {
			// COMMENT NEEDED HERE
		} else if (specialcase1) {
			vt = PAASKINDERKAMP;
			rank = 2;
		} else if (specialcase2) {
			vt = KINDERKAMP;
			rank = 3;
		} else {
			if (code.equalsIgnoreCase("G1/K1")) {
				vt = GEZINSVAKANTIE;
				rank = 1;
				vt2 = KINDERKAMP;
				rank2 = 1;
			} else if (code.equalsIgnoreCase("PK 1")) {
				vt = PAASKINDERKAMP;
				rank = 2;
			} else if (code.equalsIgnoreCase("TIKA")) {
				vt = TIENERKAMP;
				rank = 1;
			} else if (code.equalsIgnoreCase("PK 1/K1")) {
				vt = PAASKINDERKAMP;
				rank = 1;
				vt2 = KINDERKAMP;
				rank2 = 1;
			} else if (code.equalsIgnoreCase("PK 2/G2")) {
				vt = PAASKINDERKAMP;
				rank = 2;
				vt2 = GEZINSVAKANTIE;
				rank2 = 2;
			} else if (code.equalsIgnoreCase("PK 2")) {
				vt = PAASKINDERKAMP;
				rank = 2;
			} else if (code.equalsIgnoreCase("K2/3D")) {
				vt = KINDERKAMP;
				rank = 2;
				vt2 = DRIEDAAGSE;
				rank2 = 1;
			} else if (code.equalsIgnoreCase("G1/K3")) {
				vt = GEZINSVAKANTIE;
				rank = 1;
				vt2 = KINDERKAMP;
				rank2 = 3;
			} else if (code.equalsIgnoreCase("K1 (veranderd in juni)")) {
				vt = KINDERKAMP;
				rank = 1;
			} else if (code.equalsIgnoreCase("PK")) {
				vt = PAASKINDERKAMP;
				rank = 1;
			} else if (code.equalsIgnoreCase("PK1")) {
				vt = PAASKINDERKAMP;
				rank = 1;
			} else if (code.equalsIgnoreCase("3D")) {
				vt = DRIEDAAGSE;
				rank = 1;
			} else if (code.equalsIgnoreCase("PG1")) {
				vt = PAASGEZINSVAKANTIE;
				rank = 1;
			} else if (code.equalsIgnoreCase("PK2")) {
				vt = PAASKINDERKAMP;
				rank = 2;
			} else if (code.equalsIgnoreCase("G2")) {
				vt = GEZINSVAKANTIE;
				rank = 2;
			} else if (code.equalsIgnoreCase("G")) {
				vt = GEZINSVAKANTIE;
				rank = 1;
			} else if (code.equalsIgnoreCase("G1")) {
				vt = GEZINSVAKANTIE;
				rank = 1;
			} else if (code.equalsIgnoreCase("BWB1")) {
				vt = BEGELEIDWONENBRUSSEL;
				rank = 1;
			} else if (code.equalsIgnoreCase("BWB2")) {
				vt = BEGELEIDWONENBRUSSEL;
				rank = 2;
			} else if (code.equalsIgnoreCase("K3")) {
				vt = KINDERKAMP;
				rank = 3;
			} else if (code.equalsIgnoreCase("K2")) {
				vt = KINDERKAMP;
				rank = 2;
			} else if (code.equalsIgnoreCase("K1")) {
				vt = KINDERKAMP;
				rank = 1;
			} else if (code.equalsIgnoreCase("T")) {
				vt = TIENERKAMP;
				rank = 1;
			} else if (code.equalsIgnoreCase("VOV")) {
				vt = VOLWASSENVAKANTIE;
				rank = 1;
			} else if (code.equalsIgnoreCase("PG")) {
				vt = PAASGEZINSVAKANTIE;
				rank = 1;
			} else if (code.equalsIgnoreCase("X")) {
				vt = DUMMYVAKANTIE;
				rank = 1;
			} else {
				CellFeatures cf = cellVakType.getCellFeatures();
				String s = (cf == null ? "" : cf.toString());
				s += "|" + cellVakType.getCellFormat();
				throw new RuntimeException("Unknow vak.type.:[" + code + "], line: " + line + ", features: [" + s + "]");
			}
		}

		if (vt != null) {
			VakantieProject vakProj = new VakantieProject();
			String daymonth = "01/07/";
			if (rank == 2) {
				daymonth = "02/07/";
			}
			if (rank == 3) {
				daymonth = "03/07/";
			}
			vakProj.setBeginDatum(dateUtils.toDate(daymonth + jaar));
			vakProj.setEindDatum(dateUtils.toDate("30/7/" + jaar));
			vakProj.setBeginInschrijving(null);
			vakProj.setEindInschrijving(null);
			vakProj.setVakantietype(vt);

			List<VakantieProject> vpl = vakantieProjectService.findVakantieProjectWithBegindateEinddateVakantietype(vakProj);
			if (vpl.isEmpty()) {
				vakProj = vakantieProjectService.createVakantieProject(vakProj);
			} else if (vpl.size() == 1) {
				vakProj = vpl.get(0);
			} else {
				throw new RuntimeException("multiple Vakantieprojecten found.");
			}

			vp[0] = vakProj;
		}

		if (vt2 != null) {
			VakantieProject vakProj = new VakantieProject();
			String daymonth = "01/06/";
			if (rank2 == 2) {
				daymonth = "02/06/";
			}
			if (rank2 == 3) {
				daymonth = "03/06/";
			}
			vakProj.setBeginDatum(dateUtils.toDate(daymonth + jaar));
			vakProj.setEindDatum(dateUtils.toDate("29/06/" + jaar));
			vakProj.setBeginInschrijving(null);
			vakProj.setEindInschrijving(null);
			vakProj.setVakantietype(vt2);

			List<VakantieProject> vpl = vakantieProjectService.findVakantieProjectWithBegindateEinddateVakantietype(vakProj);
			if (vpl.isEmpty()) {
				vakProj = vakantieProjectService.createVakantieProject(vakProj);
			} else if (vpl.size() == 1) {
				vakProj = vpl.get(0);
			} else {
				throw new RuntimeException("multiple Vakantieprojecten found.");
			}

			vp[1] = vakProj;
		}
		// if (vp[1] != null){
		// int i1 = vp[1].getEindDatum().getYear();
		// int i2 = vp[1].getBeginDatum().getYear();
		// if (i1 != i2) {
		// LOGGER.info("jaar :[" + jaar + "],i1:[" + i1 + "],i2:[" + i2 +
		// "], line:[" + line + "], cell:" + cellVakType.getContents() + "]");
		// }
		// }
		VakantieProject[] vp2 = null;
		if (vp[0] == null) {
			return null;
		} else if (vp[1] == null) {
			vp2 = new VakantieProject[1];
			vp2[0] = vp[0];
		} else if (vp[2] == null) {
			vp2 = new VakantieProject[2];
			vp2[0] = vp[0];
			vp2[1] = vp[1];
		} else {
			vp2 = vp;
		}
		return vp2;
	}

	private void fillStaticVakatieTypes() {
		GEZINSVAKANTIE = vakantietypeService.findVakantietypeForNaam("GEZINSVAKANTIE").get(0);
		KINDERKAMP = vakantietypeService.findVakantietypeForNaam("KINDERKAMP").get(0);
		PAASKINDERKAMP = vakantietypeService.findVakantietypeForNaam("PAASKINDERKAMP").get(0);
		TIENERKAMP = vakantietypeService.findVakantietypeForNaam("TIENERKAMP").get(0);
		VOLWASSENVAKANTIE = vakantietypeService.findVakantietypeForNaam("VOLWASSENENVAKANTIE").get(0);
		DRIEDAAGSE = vakantietypeService.findVakantietypeForNaam("DRIEDAAGSE").get(0);
		BEGELEIDWONENBRUSSEL = vakantietypeService.findVakantietypeForNaam("BEGELEIDWONENBRUSSEL").get(0);
		PAASGEZINSVAKANTIE = vakantietypeService.findVakantietypeForNaam("PAASGEZINSVAKANTIE").get(0);
		DUMMYVAKANTIE = vakantietypeService.findVakantietypeForNaam("DUMMYVAKANTIE").get(0);
	}

	public boolean checkRowZero(Sheet sheet) throws UnsupportedOperationException {
		// if (!sheet.getCell(COLNO_gezinnr,
		// 0).getContents().equalsIgnoreCase("gezinsnummer")) {
		// throw new
		// UnsupportedOperationException("Input spreadsheet does not conform to expected format: headers do not match.");
		// }
		// if (!sheet.getCell(COLNO_indiv_nr,
		// 0).getContents().equalsIgnoreCase("individueel nummer")) {
		// throw new
		// UnsupportedOperationException("Input spreadsheet does not conform to expected format: headers do not match.");
		// }
		// if (!sheet.getCell(COLNO_deelnemer_voornaam,
		// 0).getContents().equalsIgnoreCase("voornaam")) {
		// throw new
		// UnsupportedOperationException("Input spreadsheet does not conform to expected format: headers do not match.");
		// }
		// if (!sheet.getCell(COLNO_opm,
		// 0).getContents().equalsIgnoreCase("opmerkingen")) {
		// throw new
		// UnsupportedOperationException("Input spreadsheet does not conform to expected format: headers do not match.");
		// }
		// ... check more ?
		//
		return true;
	}

	public boolean isEmptyRow(int rownum, Sheet sheet) {
		for (int i = 0; i < 35; i++) {
			if (!(sheet.getCell(i, rownum).getContents().isEmpty())) {
				return false;
			}
		}
		return true;
	}

	public Contactpersoon constructContactpersoonDienstFromRow(int rownum, Sheet sheet) {
		Dienst d = new Dienst();
		boolean newDienst = true;
		Contactpersoon cp = null;
		boolean compareproblems = false;
		Cell cell = sheet.getCell(COLNO_dienst_naam, rownum);
		d.setNaam(cell.getContents());
		List<Dienst> foundDiensten = dienstService.findDienstByName(d.getNaam());

		Adres a = toAdres(sheet.getCell(COLNO_dienst_straatnr, rownum).getContents(), sheet.getCell(COLNO_dienst_postcode, rownum).getContents(), sheet.getCell(COLNO_dienst_gemeenete, rownum)
				.getContents());
		if (!foundDiensten.isEmpty()) {
			for (Dienst d_compare : foundDiensten) {
				if (d_compare.getAdres() != null) {
					EqualsBuilder adresequalsbuilder = new EqualsBuilder().append(a.getGemeente(), d_compare.getAdres().getGemeente());
					adresequalsbuilder.append(a.getPostcode(), d_compare.getAdres().getPostcode());
					adresequalsbuilder.append(a.getStraat(), d_compare.getAdres().getStraat());
					adresequalsbuilder.append(a.getNummer(), d_compare.getAdres().getNummer());
					compareproblems = adresequalsbuilder.isEquals();
				} else {
					EqualsBuilder adresequalsbuilder = new EqualsBuilder().append(a.getGemeente(), "");
					adresequalsbuilder.append(a.getPostcode(), "");
					adresequalsbuilder.append(a.getStraat(), "");
					adresequalsbuilder.append(a.getNummer(), "");
					compareproblems = adresequalsbuilder.isEquals();
				}
				if (!compareproblems) {
					newDienst = false;
					d = d_compare;
				}
			}
		}
		d.setAdres(a);
		if (notNullnotEmpty(sheet.getCell(COLNO_dienst_tel, rownum).getContents())) {
			// append if already existing and different.
			String tel = sheet.getCell(COLNO_dienst_tel, rownum).getContents();
			if (notNullnotEmpty(d.getTelefoonnummer()) && !(d.getTelefoonnummer().indexOf(tel) > 0)) {
				d.setTelefoonnummer(d.getTelefoonnummer() + " OF " + tel);
			} else if (notNullnotEmpty(d.getTelefoonnummer()) && (d.getTelefoonnummer().indexOf(tel) > 0)) {
				// tel already in d.getTel.
			} else {
				d.setTelefoonnummer(sheet.getCell(COLNO_dienst_tel, rownum).getContents());
			}
		}
		if (notNullnotEmpty(sheet.getCell(COLNO_dienst_email, rownum).getContents())) {
			// append if already existing and different.
			String email = sheet.getCell(COLNO_dienst_email, rownum).getContents();
			if (notNullnotEmpty(d.getEmailadres()) && !(d.getEmailadres().indexOf(email) > 0)) {
				d.setEmailadres(d.getEmailadres() + " OF " + email);
			} else if (notNullnotEmpty(d.getEmailadres()) && (d.getEmailadres().indexOf(email) > 0)) {
				// email already in d.getemail.
			} else {
				d.setEmailadres(email);
			}
		}
		// if (notNullnotEmpty(sheet.getCell(COLNO_dienst_fax,
		// rownum).getContents())) {
		// d.setFaxnummer(sheet.getCell(COLNO_dienst_fax,
		// rownum).getContents());
		// }
		cp = toContactpersoon(sheet.getCell(COLNO_dienst_contactpers, rownum).getContents());
		boolean cpFound = false;
		if (!newDienst) {
			for (Contactpersoon c2 : d.getContactpersonen()) {
				EqualsBuilder cpEq = new EqualsBuilder();
				cpEq.append(cp.getFamilienaam(), c2.getFamilienaam());
				cpEq.append(cp.getVoornaam(), c2.getVoornaam());
				// the other fields are empty for this CP.
				if (cpEq.isEquals()) {
					cpFound = true;
					cp = c2;
				}
			}
		}

		if (newDienst) {
			cp.setDienst(d);
			d = dienstService.update(d);
			cp = contactpersoonService.update(cp);
		}
		if (!newDienst && !cpFound) {
			cp.setDienst(d);
			d = dienstService.update(d);
			cp = contactpersoonService.update(cp);
		}
		if (!newDienst && cpFound) {
			d = dienstService.update(d);
		}
		return cp;
	}

	public ContactType constructContactTypeFromRow(int rownum, Sheet sheet) {
		String ctString = sheet.getCell(COLNO_aanvraag_contactType, rownum).getContents();
		if (ctString.trim().equalsIgnoreCase("dienst")) {
			return ContactType.Dienst;
		}
		if (ctString.trim().equalsIgnoreCase("dienst + gezin")) {
			return ContactType.GezinDienst;
		}
		if (ctString.trim().equalsIgnoreCase("gezin")) {
			return ContactType.Gezin;
		}
		if (ctString.trim().equalsIgnoreCase("gezin / dienst")) {
			return ContactType.GezinDienst;
		}
		if (ctString.trim().equalsIgnoreCase("")) {
			return null;
		}
		LOGGER.info("Contacttype not recognised: [" + ctString + "]");
		return null;
	}

	public Status constructStatus2012FromRow(int rownum, Sheet sheet) {
		String statusString = sheet.getCell(COLNO_2012_status, rownum).getContents();
		if (statusString.trim().equalsIgnoreCase("bevestigd")) {
			return Status.BEVESTIGD;
		}
		if (statusString.trim().equalsIgnoreCase("annulatie")) {
			return Status.GEANNULEERDPIRLEWIET;
		}
		if (statusString.trim().equalsIgnoreCase("geannuleerd")) {
			return Status.GEANNULEERDDEELNEMER;
		}
		if (statusString.trim().equalsIgnoreCase("bevestigd PK 2")) {
			return Status.BEVESTIGD; // vakje 2012 bevat in dit geval PK2/K3 we
										// splitsen die naar PK2 in 2012 en K3
										// in 2012_II, waarij we dan voor de
										// eerste bevestigd geven, en voor de 2e
										// geannulleerd.
		}
		if (statusString.trim().equalsIgnoreCase("geannuleerd via VP")) {
			return Status.GEANNULEERDPIRLEWIET;
		}
		if (statusString.trim().equalsIgnoreCase("geweigerd")) {
			return Status.WEIGERING;
		}
		if (statusString.trim().equalsIgnoreCase("VP annuleert")) {
			return Status.GEANNULEERDDEELNEMER;
		}
		if (statusString.trim().equalsIgnoreCase("Wachtlijst")) {
			return Status.WACHTLIJST;
		}
		if (statusString.trim().equalsIgnoreCase("wachtlijst - annulatie")) {
			return Status.GEANNULEERDDEELNEMER;
		}
		if (statusString.trim().equalsIgnoreCase("wachtlijst 1")) {
			return Status.WACHTLIJST;
		}
		if (statusString.trim().equalsIgnoreCase("wachtlijst 2")) {
			return Status.WACHTLIJST;
		}
		if (statusString.trim().equalsIgnoreCase("wachtlijst2")) {
			return Status.WACHTLIJST;
		}
		if (statusString.trim().equalsIgnoreCase("")) {
			return Status.INBEHANDELING;
		}
		LOGGER.info("statustype not recognised: [" + statusString + "]");
		return null;
	}

	public Status constructStatus2012IIFromRow(int rownum, Sheet sheet) {
		String statusString = sheet.getCell(COLNO_2012_status, rownum).getContents();
		if (statusString.trim().equalsIgnoreCase("bevestigd")) {
			return Status.BEVESTIGD;
		}
		if (statusString.trim().equalsIgnoreCase("annulatie")) {
			return Status.GEANNULEERDPIRLEWIET;
		}
		if (statusString.trim().equalsIgnoreCase("geannuleerd")) {
			return Status.GEANNULEERDDEELNEMER;
		}
		if (statusString.trim().equalsIgnoreCase("bevestigd PK 2")) {
			return Status.WEIGERING; // vakje 2012 bevat in dit geval PK2/K3 we
										// splitsen die naar PK2 in 2012 en K3
										// in 2012_II, waarij we dan voor de
										// eerste bevestigd geven, en voor de 2e
										// geweigerd.
		}
		if (statusString.trim().equalsIgnoreCase("geannuleerd via VP")) {
			return Status.GEANNULEERDPIRLEWIET;
		}
		if (statusString.trim().equalsIgnoreCase("geweigerd")) {
			return Status.WEIGERING;
		}
		if (statusString.trim().equalsIgnoreCase("VP annuleert")) {
			return Status.GEANNULEERDDEELNEMER;
		}
		if (statusString.trim().equalsIgnoreCase("Wachtlijst")) {
			return Status.WACHTLIJST;
		}
		if (statusString.trim().equalsIgnoreCase("wachtlijst - annulatie")) {
			return Status.GEANNULEERDDEELNEMER;
		}
		if (statusString.trim().equalsIgnoreCase("wachtlijst 1")) {
			return Status.WACHTLIJST;
		}
		if (statusString.trim().equalsIgnoreCase("wachtlijst 2")) {
			return Status.WACHTLIJST;
		}
		if (statusString.trim().equalsIgnoreCase("wachtlijst2")) {
			return Status.WACHTLIJST;
		}
		if (statusString.trim().equalsIgnoreCase("")) {
			return Status.INBEHANDELING;
		}
		LOGGER.info("statustype not recognised: [" + statusString + "]");
		return null;
	}

	public Deelnemer constructDeelnemerFromRow(int rownum, Sheet sheet) {
		Deelnemer d = new Deelnemer();
		// d.setRijksregisternr(sheet.getCell(COLNO_indiv_nr,
		// rownum).getContents());
		d.setVoornaam(sheet.getCell(COLNO_deelnemer_voornaam, rownum).getContents());
		d.setFamilienaam(sheet.getCell(COLNO_deelnemer_famnaam, rownum).getContents());
		String sex = sheet.getCell(COLNO_deelnemer_mv, rownum).getContents();
		sex = sex.toUpperCase();
		d.setGeslacht(Geslacht.Onbekend);
		if (sex.equals("M")) {
			d.setGeslacht(Geslacht.Man);
		}
		if (sex.equals("V")) {
			d.setGeslacht(Geslacht.Vrouw);
		}
		Date gebdate = null;
		if (notNullnotEmpty(sheet.getCell(COLNO_deelnemer_gebdate, rownum).getContents())) {
			gebdate = dateUtils.toDate(sheet.getCell(COLNO_deelnemer_gebdate, rownum).getContents());
		}
		d.setGeboortedatum(gebdate);
		d.setTelefoonnr(sheet.getCell(COLNO_deelnemer_tel, rownum).getContents());

		return d;
	}

	public Adres constructAdresDeelnemerFromRow(int rownum, Sheet sheet) {
		Adres a = toAdres(sheet.getCell(COLNO_aanvraag_adres_straatnr, rownum).getContents(), sheet.getCell(COLNO_aanvraag_adres_postcode, rownum).getContents(),
				sheet.getCell(COLNO_aanvraag_adres_gemeente, rownum).getContents());
		return a;
	}

	public boolean notNullnotEmpty(String s) {
		if (s == null) {
			return false;
		}
		if (s.isEmpty()) {
			return false;
		}
		return true;
	}

	public Contactpersoon toContactpersoon(String naam) {
		Contactpersoon c = new Contactpersoon();
		String[] s = splitNaamInVoornaamAchternaam(naam);
		c.setVoornaam(s[0]);
		c.setFamilienaam(s[1]);
		return c;
	}

	public String[] splitNaamInVoornaamAchternaam(String naam) {
		String[] s = new String[2];
		if (naam.contains(" ")) {
			s[0] = naam.substring(0, naam.indexOf(" "));
			s[1] = naam.substring(naam.indexOf(" ") + 1);
			if (!(s[0] + " " + s[1]).contentEquals(naam)) {
				throw new RuntimeException("voornaam+familienaam NE naam!");
			}
		} else {
			s[0] = null;
			s[1] = naam;
		}

		return s;
	}

	public Adres toAdres(String straatnummer, String postcode, String gemeente) {
		Adres a = new Adres();
		a.setGemeente(gemeente);
		a.setPostcode(postcode);
		if (straatnummer == null) {
			a.setNummer(null);
			a.setStraat(null);
			return a;
		}
		String parts[] = straatnummer.split(" ");
		if (parts.length == 0) {
			a.setNummer(null);
			a.setStraat(null);
		} else if (parts.length > 0) {
			// find the first piece starting with a num ?
			int firstpartofnummer = -1;
			for (int i = 0; i < parts.length; i++) {
				if ((parts[i].length() > 0) && (Character.isDigit(parts[i].charAt(0)))) {
					firstpartofnummer = i;
					break;
				}
			}

			if (firstpartofnummer < 0) {
				for (int i = 0; i < parts.length; i++) {
					if ((parts[i].length() > 0) && (Character.isDigit(parts[i].charAt(parts[i].length() - 1)))) {
						firstpartofnummer = i;
						break;
					}
				}
			}

			if (firstpartofnummer < 0) {
				// we put the complete thing in straat
				firstpartofnummer = parts.length;
			}

			if (firstpartofnummer > -1) {
				String straat = "";
				for (int j = 0; j < firstpartofnummer; j++) {
					if (j > 0) {
						straat += " ";
					}
					straat += parts[j];
				}
				String nummer = "";
				for (int j = firstpartofnummer; j < parts.length; j++) {
					if (j > firstpartofnummer) {
						nummer += " ";
					}
					nummer += parts[j];
				}
				a.setNummer(nummer);
				a.setStraat(straat);
			}
		}
		// if ( notNullnotEmpty(straatnummer)) {
		// System.out.print("input :[" + straatnummer + "]");
		// System.out.print(" straat:[" + a.getStraat() + "]");
		// LOGGER.info(" nummer:[" + a.getNummer() + "]");
		// }
		String reconstructedInputStraat = "";
		String recnr = a.getNummer();
		if (recnr == null) {
			recnr = "";
		}
		String gemnm = a.getStraat();
		if (gemnm == null) {
			gemnm = "";
		}
		if (!gemnm.isEmpty() && !recnr.isEmpty()) {
			reconstructedInputStraat = gemnm + " " + recnr;
		} else if (gemnm.isEmpty() && !recnr.isEmpty()) {
			reconstructedInputStraat = recnr;
		} else if (!gemnm.isEmpty() && recnr.isEmpty()) {
			reconstructedInputStraat = gemnm;
		} else {
		}
		if (!reconstructedInputStraat.isEmpty() || !straatnummer.isEmpty()) {
			if (!reconstructedInputStraat.trim().equals(straatnummer.trim()) && !(reconstructedInputStraat.trim() + " ").equals(straatnummer.trim())) {
				LOGGER.info("input/output NE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! reconstr:[" + reconstructedInputStraat + "]in:[" + straatnummer + "]");
				throw new RuntimeException("Straat import/reconstructed Not Equals!!!");
			}
		}
		return a;
	}
}
