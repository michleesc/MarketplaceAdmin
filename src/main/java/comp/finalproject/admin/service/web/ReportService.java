package comp.finalproject.admin.service.web;

import comp.finalproject.admin.entity.Sale;
import comp.finalproject.admin.repository.SalesRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Service
public class ReportService {
    @Autowired
    private DataSource dataSource; // DataSource untuk mengakses database
    @Autowired
    private SalesRepository salesRepository;
    @Autowired
    private ResourceLoader resourceLoader;

    public void reportPdfAll(OutputStream outputStream) throws JRException {
        List<Sale> sales = salesRepository.findByDeletedFalseOrderByIdDesc();

        if (sales.isEmpty()) {
            throw new RuntimeException("No data found for the provided User ID."); // Optional message for empty data
        }

        try {
            File file = ResourceUtils.getFile("classpath:penjualan.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(sales);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Michlee");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error generating report.", e);
        }
    }

    public void reportPdfPerDate(OutputStream outputStream, Date startDate, Date endDate) throws JRException{
        List<Sale> sales;

        if (startDate != null && endDate != null) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            endCalendar.set(Calendar.HOUR_OF_DAY, 23);
            endCalendar.set(Calendar.MINUTE, 59);
            endCalendar.set(Calendar.SECOND, 59);

            sales = salesRepository.findByDeletedFalseAndDateBetweenOrderByIdDesc(startDate, endCalendar.getTime());
            System.out.println(startDate);
            System.out.println(endDate);
        } else if (startDate != null && endDate == null) {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(startDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            Date endDateIsNull = new java.sql.Date(calendar.getTimeInMillis());

            sales = salesRepository.findByDeletedFalseAndDateBetweenOrderByIdDesc(startDate, endDateIsNull);
            System.out.println(startDate);
            System.out.println(endDate);
        } else {
            sales = salesRepository.findByDeletedFalseOrderByIdDesc();
            System.out.println(startDate);
            System.out.println(endDate);
        }

        try {
            File file = ResourceUtils.getFile("classpath:penjualanPerDate.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(sales);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Michlee");
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error generating report.", e);
        }
    }
}
