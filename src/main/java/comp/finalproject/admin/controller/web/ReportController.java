package comp.finalproject.admin.controller.web;

import comp.finalproject.admin.service.web.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/reportPdfAll")
    public void reportPdfAll(HttpServletResponse response) throws JRException, IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=laporan.pdf");
        reportService.reportPdfAll(response.getOutputStream());
    }

    @GetMapping("/reportPdfPerDate")
    public void reportPdfPerDate(@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                 @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                 HttpServletResponse response) throws JRException, IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=laporan.pdf");
        reportService.reportPdfPerDate(response.getOutputStream(), startDate, endDate);
    }
}