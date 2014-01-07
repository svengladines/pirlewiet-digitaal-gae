package be.pirlewiet.registrations.view.controllers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import be.pirlewiet.registrations.services.XlsReportService;

@Controller
@RequestMapping("secretariaat/rapporten")
public class XlsReportController {
    
    private static final Logger LOGGER = Logger.getLogger("XlsReportController");
    
    @Autowired
    private XlsReportService xlsReportService;
        
    @RequestMapping("/inschrijvingsaanvragen-klassiek.xls")
    public void downloadXlsReportClassic(HttpServletResponse response, Model model) {
        LOGGER.info("/secretariaat/rapporten/inschrijvingsaanvragen-klassiek.xls");
        Workbook workbook = xlsReportService.generateBackUpXls();
        ServletOutputStream outputStream;
        try {
            response.setContentType("application/vnd.ms-excel");
            outputStream = response.getOutputStream();     
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }     
    
    @RequestMapping("xls/klassiek/browser")
    public ModelAndView viewXlsReportClassic(HttpServletResponse response, Model model){        
        Workbook workbook = xlsReportService.generateBackUpXls();
        return new ModelAndView();
        
    }
}
