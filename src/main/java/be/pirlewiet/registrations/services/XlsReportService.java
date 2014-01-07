
package be.pirlewiet.registrations.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import be.pirlewiet.registrations.model.AanvraagInschrijving;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.utils.PropertyChecker;

@Service
public class XlsReportService {
    
    private static final Logger LOGGER = Logger.getLogger("XlsReportService");
    
    private String headerGezinsNummer = "gezinsnummer";
    private String headerDeelnemerVoornaam = "voornaam";
    private String headerDeelnemerFamilienaam = "familienaam";
    private String headerDeelnemerGeboortedatum = "geboortedatum";
    private String headerDienstNaam = "naam dienst";
    private String headerDienstAdres = "adres dienst";
    private String headerDienstPostcode = "postcode dienst";
    private String headerDienstGemeente = "gemeente dienst";
    private String headerDienstTelefoon = "telefoon dienst";
    private String headerDienstEmail = "e-mail dienst";
    private String headerDienstFax = "fax dienst";
    private String headerDienstContactpersoon = "contactpersoon dienst";
    private String headerDeelnemerAdres = "adres thuis";
    private String headerDeelnemerPostcode = "postcode thuis";
    private String headerDeelnemerGemeente = "gemeente thuis";
    private String headerDeelnemerTelefoon = "telefoon thuis";
    private String headerContactType = "contact via dienst/gezin";
    private String headerVakantieproject = "2012";
    private String headerInschrijvingsAanvraagStatus = "status";
      
    @Autowired
    private InschrijvingService inschrijvingService;
    
    @Transactional
    public Workbook generateBackUpXls(){
        //creeer workbook
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        
        //set headers
        int rowIndex = 0;
        fillRow(sheet, rowIndex, createHeaderMap());
        
        //retrieve data
        List<AanvraagInschrijving> inschrijvingen = inschrijvingService.getInschrijvingen();
        fillRows(inschrijvingen, ++rowIndex, sheet);
        return workbook;
    }
    
    //fill rows, whilst iterating data
    private void fillRows(final List<AanvraagInschrijving> inschrijvingen, final int firstRowIndex, Sheet sheet) {
        int rowIndex = firstRowIndex;
        HashMap<Integer, String> rowData;
        for (Iterator<AanvraagInschrijving> it = inschrijvingen.iterator(); it.hasNext();) {
            AanvraagInschrijving aanvraagInschrijving = it.next(); 
            rowData = createRowDataMap(aanvraagInschrijving);
            fillRow(sheet, rowIndex, rowData);
            rowIndex++;
        }
    } 
            
    public void fillRow(Sheet sheet, final int rowIndex, final HashMap<Integer, String> rowDataMap){
        
        //getRow, creating it when needed
        Row row = CellUtil.getRow(rowIndex, sheet);  
        Cell cell;
        for (Map.Entry<Integer, String> entry : rowDataMap.entrySet()) {
            Integer columnIndex = entry.getKey();
            String cellValue = entry.getValue();
            cell = CellUtil.getCell(row, columnIndex);
            cell.setCellValue(cellValue);
            }                      
    }
    /**
     * Creates a map<columnNumber, cellValue>.
     * Argument value aanvraagInschrijving cannot be NULL.
     * Argument property value aanvraagInschrijving.deelnemers cannot be empty.
     * Argument property value aanvraagInschrijving.contactPersoon cannot be NULL.
     * @param aanvraagInschrijving
     * @return 
     */
    public HashMap<Integer, String> createRowDataMap(final AanvraagInschrijving aanvraagInschrijving){
        Assert.notNull(aanvraagInschrijving, "Argument value aanvraagInschrijving cannot be NULL.");
        Assert.notEmpty(aanvraagInschrijving.getDeelnemers(), "Argument property value deelnemers cannot be emtpy. At least one deelnemer should be associated with aanvraagInschrijving");
        Assert.notNull(aanvraagInschrijving.getContactpersoon(), "Argument property value contactPersoon cannot be NULL.");
        
        HashMap<Integer, String> rowDataMap = new HashMap<Integer, String>();
        List<Deelnemer> deelnemers = aanvraagInschrijving.getDeelnemers();
        
        Contactpersoon contactPersoon = aanvraagInschrijving.getContactpersoon();
        Dienst dienst = contactPersoon.getDienst();
        
        Adres dienstAdres = new Adres();        
        if(dienst!=null){
            dienstAdres = dienst.getAdres();
        } 
        
        Adres deelnemersAdres = new Adres();
        if (aanvraagInschrijving.getDeelnemersAdres()!=null){
            deelnemersAdres = aanvraagInschrijving.getDeelnemersAdres();
        }
        		
	for (Deelnemer deelnemer : deelnemers) {
            LOGGER.info("voornaam/naam: " + deelnemer.getVoornaam() + "/" + deelnemer.getFamilienaam());
            rowDataMap.put(1, PropertyChecker.getAsString(deelnemer, "voornaam", ""));
            rowDataMap.put(2, PropertyChecker.getAsString(deelnemer, "familienaam", ""));
            rowDataMap.put(3, PropertyChecker.getAsString(deelnemer, "geboortedatum", ""));
            rowDataMap.put(4, PropertyChecker.getAsString(dienst, "naam", ""));
            rowDataMap.put(5, PropertyChecker.getAsString(dienstAdres, "straat", "") + " " + PropertyChecker.getAsString(dienstAdres, "nummer", ""));
            rowDataMap.put(6, PropertyChecker.getAsString(dienstAdres, "postcode", ""));
            rowDataMap.put(7, PropertyChecker.getAsString(dienstAdres, "gemeente", ""));
            rowDataMap.put(8, PropertyChecker.getAsString(dienst, "telefoonnummer", ""));
            rowDataMap.put(9, PropertyChecker.getAsString(dienst, "emailadres", ""));
            rowDataMap.put(10, PropertyChecker.getAsString(dienst, "faxnummer", ""));
            rowDataMap.put(11, PropertyChecker.getAsString(contactPersoon, "voornaam", "") + " " + PropertyChecker.getAsString(contactPersoon, "familienaam", ""));
            rowDataMap.put(12, PropertyChecker.getAsString(deelnemersAdres, "straat", "") + " " + PropertyChecker.getAsString(deelnemersAdres, "nummer", ""));
            rowDataMap.put(13, PropertyChecker.getAsString(deelnemersAdres, "postcode", ""));
            rowDataMap.put(14, PropertyChecker.getAsString(deelnemersAdres, "gemeente", ""));
            rowDataMap.put(15, PropertyChecker.getAsString(deelnemer, "telefoonnr", ""));
            rowDataMap.put(16, PropertyChecker.getAsString(aanvraagInschrijving, "contactType", ""));
	}
        
        return rowDataMap;
    }
        
    private HashMap createHeaderMap(){
        HashMap headerMap = new HashMap();
        headerMap.put(0, headerGezinsNummer);
        headerMap.put(1, headerDeelnemerVoornaam);
        headerMap.put(2, headerDeelnemerFamilienaam);
        headerMap.put(3, headerDeelnemerGeboortedatum);
        headerMap.put(4, headerDienstNaam);
        headerMap.put(5, headerDienstAdres);
        headerMap.put(6, headerDienstPostcode);
        headerMap.put(7, headerDienstGemeente);
        headerMap.put(8, headerDienstTelefoon);
        headerMap.put(9, headerDienstEmail);
        headerMap.put(10, headerDienstFax);
        headerMap.put(11, headerDienstContactpersoon);
        headerMap.put(12, headerDeelnemerAdres);
        headerMap.put(13, headerDeelnemerPostcode);
        headerMap.put(14, headerDeelnemerGemeente);
        headerMap.put(15, headerDeelnemerTelefoon);
        headerMap.put(16, headerContactType);
               
        return headerMap;
    }
 
       
}
