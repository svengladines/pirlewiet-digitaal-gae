
package be.pirlewiet.registrations.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import be.pirlewiet.registrations.model.AanvraagInschrijving;
import be.pirlewiet.registrations.model.Adres;
import be.pirlewiet.registrations.model.ContactType;
import be.pirlewiet.registrations.model.Contactpersoon;
import be.pirlewiet.registrations.model.Deelnemer;
import be.pirlewiet.registrations.model.Dienst;
import be.pirlewiet.registrations.services.XlsReportService;


public class XlsReportServiceTest {
    
    private static final Logger LOGGER = Logger.getLogger(XlsReportService.class.getName());
      
    @InjectMocks
    private XlsReportService xlsReportService;
                
    @Before
    public void init(){
       MockitoAnnotations.initMocks(this);           
    }
        
    @Test
    public void testFillRow(){             
       
       Workbook workbook = new HSSFWorkbook();
       Sheet sheet = workbook.createSheet();
       
       HashMap<Integer, String> rowDataMap = xlsReportService.createRowDataMap(createAanvraagInschrijving());
       Deelnemer deelnemer = createDeelnemer();
       
       xlsReportService.fillRow(sheet, 1, rowDataMap);
       Row row = sheet.getRow(1);
       
       int numberOfCells = row.getPhysicalNumberOfCells();
       LOGGER.info(Integer.toString(numberOfCells));
       LOGGER.info(row.getCell(2).getStringCellValue());
       
       assertEquals(deelnemer.getVoornaam(), row.getCell(1).getStringCellValue()); 
       assertEquals(deelnemer.getFamilienaam(), row.getCell(2).getStringCellValue());
       //Peter, nog na te kijken???
//       assertEquals(deelnemer.getGeboortedatum().toString(), row.getCell(3).getStringCellValue());    
       
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateRowMapThrowsIllegalArgumentExceptionWhenAanvraagInschrijvingNull(){
        AanvraagInschrijving aanvraagInschrijving = null;
        xlsReportService.createRowDataMap(aanvraagInschrijving);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateRowMapThrowsIllegalArgumentExceptionWhenAanvraagInschrijvingNoDeelnemers(){
        AanvraagInschrijving aanvraagInschrijving = new AanvraagInschrijving();
        xlsReportService.createRowDataMap(aanvraagInschrijving);
    }
        
    private AanvraagInschrijving createAanvraagInschrijving(){
        
       AanvraagInschrijving aanvraagInschrijving = new AanvraagInschrijving();
       Adres adres = createAdres();
       Deelnemer deelnemer = createDeelnemer();  
       Dienst dienst = createDienst(adres);
       Contactpersoon contactPersoon = createContactpersoon(dienst);
       
       aanvraagInschrijving.setDeelnemers(new ArrayList<Deelnemer>(Arrays.asList(deelnemer)));     
       aanvraagInschrijving.setContactpersoon(contactPersoon);
       
       aanvraagInschrijving.setContactType(ContactType.Dienst);
       aanvraagInschrijving.setDeelnemersAdres(adres);
       return aanvraagInschrijving;
        
    }
    
    private Deelnemer createDeelnemer(){
       Deelnemer deelnemer = new Deelnemer();
       deelnemer.setVoornaam("Baron");
       deelnemer.setFamilienaam("Van Stiepelsteen");
       deelnemer.setGeboortedatum(new Date());
       
       return deelnemer;
    }
    
    private Adres createAdres(){
       Adres adres = new Adres();
       adres.setStraat("Bommelweg");
       adres.setNummer("12");
       adres.setPostcode("3210");
       adres.setGemeente("Zonnedorp");
       
       return adres;
    }
    
    private Dienst createDienst(Adres adres){
       Dienst dienst = new Dienst();
       dienst.setAdres(adres);
       dienst.setEmailadres("flup@flap.com");
       dienst.setNaam("OCMW Zaffelare");
       dienst.setTelefoonnummer("023456789");
       dienst.setFaxnummer("098765432");
       
       return dienst;
    }
    
    private Contactpersoon createContactpersoon(Dienst dienst){
        Contactpersoon contactPersoon = new Contactpersoon();
       contactPersoon.setVoornaam("Jan");
       contactPersoon.setFamilienaam("Haring");
       contactPersoon.setDienst(dienst);
       return contactPersoon;
    }
    
}
