package comp.finalproject.admin.service.web;

import comp.finalproject.admin.entity.Sale;
import comp.finalproject.admin.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Component("webPageService")
public class PageService {
    @Autowired
    private SalesRepository salesRepository;

    public double calculatePercentageChangeYesterday() {
        // Mendapatkan data penjualan sebelumnya (misalnya hari sebelumnya)
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Date yesterdayStartDate = java.sql.Date.valueOf(yesterday);
        Date yesterdayEndDate = java.sql.Date.valueOf(yesterday.plusDays(1));

        List<Sale> previousSales = salesRepository.findByDateBetween(yesterdayStartDate, yesterdayEndDate);

        // Menghitung total dari subtotal penjualan sebelumnya
        double previousTotal = previousSales.stream().mapToDouble(Sale::getSubtotal).sum();

        // Mendapatkan data penjualan hari ini
        LocalDate today = LocalDate.now();
        Date startDate = java.sql.Date.valueOf(today);
        Date endDate = java.sql.Date.valueOf(today.plusDays(1));

        List<Sale> currentSales = salesRepository.findByDateBetween(startDate, endDate);

        // Menghitung total dari subtotal penjualan hari ini
        double currentTotal = currentSales.stream().mapToDouble(Sale::getSubtotal).sum();

        // Menghitung persentase perubahan
        return ((currentTotal - previousTotal) / previousTotal) * 100;
    }

    public List<Sale> getCurrentSales() {
        // Mendapatkan data penjualan hari ini
        LocalDate today = LocalDate.now();
        Date startDate = java.sql.Date.valueOf(today);
        Date endDate = java.sql.Date.valueOf(today.plusDays(1));

        List<Sale> currentSales = salesRepository.findByDateBetweenOrderByIdDesc(startDate, endDate);

        return currentSales;
    }

    public int getPreviousSalesCount() {
        // Mendapatkan data penjualan sebelumnya (misalnya hari sebelumnya)
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Date yesterdayStartDate = java.sql.Date.valueOf(yesterday);
        Date yesterdayEndDate = java.sql.Date.valueOf(yesterday.plusDays(1));

        List<Sale> previousSales = salesRepository.findByDateBetween(yesterdayStartDate, yesterdayEndDate);

        return previousSales.size();
    }

    public int getCurrentSalesCount() {
        // Mendapatkan data penjualan hari ini
        List<Sale> currentSales = getCurrentSales();
        return currentSales.size();
    }

    public double calculateSalesComparisonPercentage() {
        int previousSalesCount = getPreviousSalesCount();
        int currentSalesCount = getCurrentSalesCount();

        // Menghitung persentase perubahan penjualan
        return ((currentSalesCount - previousSalesCount) / (double) previousSalesCount) * 100;
    }
}
