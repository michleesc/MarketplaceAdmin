package comp.finalproject.admin.controller.web;

import comp.finalproject.admin.entity.Item;
import comp.finalproject.admin.entity.Sale;
import comp.finalproject.admin.entity.User;
import comp.finalproject.admin.repository.ItemRepository;
import comp.finalproject.admin.repository.SalesRepository;
import comp.finalproject.admin.service.web.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class SaleController {

    @Autowired
    private SalesRepository salesRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    UserService userService;

    @GetMapping("/sales")
    public String viewAllSales(Model model, Principal principal) {
        String email = principal.getName();

        User user = userService.findUserByEmail(email);
        String name = user.getName();

        // Menambahkan atribut firstName ke objek Model
        model.addAttribute("email", email);
        model.addAttribute("name", name);

        List<Sale> allSales = salesRepository.findAllByOrderByIdDesc();
        model.addAttribute("sales", allSales);

        List<String> statusList = Arrays.asList("Menunggu", "Success");
        model.addAttribute("statusList", statusList);

        return "sale/sales";
    }

    @GetMapping("/sales/search")
    public String searchByDate(@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                               @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                               Model model, Principal principal) {
        List<Sale> data;
        String email = principal.getName();

        User user = userService.findUserByEmail(email);
        String name = user.getName();

        // Menambahkan atribut firstName ke objek Model
        model.addAttribute("email", email);
        model.addAttribute("name", name);

        // update
        List<String> statusList = Arrays.asList("Menunggu", "Success");
        model.addAttribute("statusList", statusList);
        // searching
        if (startDate != null && endDate != null) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            // Set waktu ke akhir hari
            endCalendar.set(Calendar.HOUR_OF_DAY, 23);
            endCalendar.set(Calendar.MINUTE, 59);
            endCalendar.set(Calendar.SECOND, 59);

            // Jika pencarian berhasil
            data = salesRepository.findByDateBetween(startDate, endCalendar.getTime());
        } else if (startDate != null && endDate == null) {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(startDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            Date endDateIsNull = new java.sql.Date(calendar.getTimeInMillis());

            data = salesRepository.findByDateBetween(startDate, endDateIsNull);
        } else {
            // jika pencarian gagal, tampilkan semuanya
            data = salesRepository.findAllByOrderByIdDesc();
        }
        model.addAttribute("sales", data);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "sale/listsales";

    }

    @PostMapping("/updatesalestatus/{saleId}")
    public String updateSaleStatus(@PathVariable("saleId") long saleId, @RequestParam("status") String status) {
        Sale sale = salesRepository.findById(saleId);
        if (sale != null) {
            sale.setStatus(status);
            salesRepository.save(sale);
        }
        return "redirect:/sales";
    }

    @GetMapping("/deletesales/{id}")
    public String deleteSale(@PathVariable("id") long id) {
        Sale sale = salesRepository.findById(id);
        if (sale != null) {
            // Mengembalikan total_sold dan quantity item ke nilai semula
            Item item = sale.getItem();
            int quantity = sale.getQuantity();

            item.setTotalSold(item.getTotalSold() - quantity);
            item.setQuantity(item.getQuantity() + quantity);
            itemRepository.save(item);

            // Menghapus penjualan dari repository
            salesRepository.deleteById(id);
        }
        return "redirect:/sales";
    }

//    @GetMapping("/generate-laporan-search")
//    public void generateLaporanPdf(@RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
//                                   @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
//                                   HttpServletResponse response) {
//        List<Sale> allSales;
//        if (startDate != null && endDate != null) {
//            Calendar startCalendar = Calendar.getInstance();
//            startCalendar.setTime(startDate);
//
//            Calendar endCalendar = Calendar.getInstance();
//            endCalendar.setTime(endDate);
//            endCalendar.set(Calendar.HOUR_OF_DAY, 23);
//            endCalendar.set(Calendar.MINUTE, 59);
//            endCalendar.set(Calendar.SECOND, 59);
//
//            allSales = salesRepository.findByDateBetween(startDate, endCalendar.getTime());
//        } else if (startDate != null && endDate == null) {
//            Calendar calendar = Calendar.getInstance();
//
//            calendar.setTime(startDate);
//            calendar.set(Calendar.HOUR_OF_DAY, 23);
//            calendar.set(Calendar.MINUTE, 59);
//            calendar.set(Calendar.SECOND, 59);
//            Date endDateIsNull = new java.sql.Date(calendar.getTimeInMillis());
//
//            allSales = salesRepository.findByDateBetween(startDate, endDateIsNull);
//        } else {
//            allSales = salesRepository.findByDeletedFalseOrderByIdDesc();
//        }
//
//        if (allSales.isEmpty()) {
//            System.out.println("Gagal menemukan Penjualan");
//        }
//
//        try {
//            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(response.getOutputStream()));
//            Document document = new Document(pdfDocument);
//
//            // Menentukan nama file PDF dengan menggunakan tanggal
//            String fileName = "Laporan_" + new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() + ".pdf";
//
//            response.setContentType("application/pdf");
//            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
//
//            DecimalFormat decimalFormat = new DecimalFormat("#,##0");
//            LocalDate startDateNew = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//            LocalDate endDateNew = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//
//            document.add(new Paragraph("Periode : " + startDateNew + " - " + endDateNew));
//
//            // Membuat tabel untuk data penjualan
//            Table table = new Table(4);  // Sesuaikan dengan jumlah atribut penjualan
//            table.setHorizontalAlignment(HorizontalAlignment.CENTER); // Menengahkan tabel
//
//            // Menambahkan baris dengan judul
//            table.addCell("Tanggal Penjualan");
//            table.addCell("Item");
//            table.addCell("Jumlah");
//            table.addCell("Total Harga");
//
//            // Menambahkan baris dengan data penjualan
//            for (Sale sale : allSales) {
//                LocalDate saleDate = sale.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                table.addCell(saleDate.toString());
//                table.addCell(String.valueOf(sale.getItem().getName()));
//                table.addCell(String.valueOf(sale.getQuantity()));
//                table.addCell(String.valueOf(decimalFormat.format(sale.getSubtotal())));
//            }
//
//            // Menambahkan tabel ke dokumen
//            document.add(table);
//
//            // Menutup dokumen
//            document.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @GetMapping("/generate-laporan")
//    public void generateLaporanPdfAll(
//            HttpServletResponse response) {
//        List<Sale> allSales = salesRepository.findByDeletedFalseOrderByIdDesc();
//
//        if (allSales.isEmpty()) {
//            System.out.println("Gagal menemukan Penjualan");
//        }
//
//        try {
//            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(response.getOutputStream()));
//            Document document = new Document(pdfDocument);
//
//            // Menentukan nama file PDF dengan menggunakan tanggal
//            String fileName = "Laporan_" + new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() + ".pdf";
//
//            response.setContentType("application/pdf");
//            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
//
//            DecimalFormat decimalFormat = new DecimalFormat("#,##0");
//
//            document.add(new Paragraph("Periode : " + new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
//
//            // Membuat tabel untuk data penjualan
//            Table table = new Table(4);  // Sesuaikan dengan jumlah atribut penjualan
//            table.setHorizontalAlignment(HorizontalAlignment.CENTER); // Menengahkan tabel
//
//            // Menambahkan baris dengan judul
//            table.addCell("Tanggal Penjualan");
//            table.addCell("Item");
//            table.addCell("Jumlah");
//            table.addCell("Total Harga");
//
//            // Menambahkan baris dengan data penjualan
//            for (Sale sale : allSales) {
//                LocalDate saleDate = sale.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                table.addCell(saleDate.toString());
//                table.addCell(String.valueOf(sale.getItem().getName()));
//                table.addCell(String.valueOf(sale.getQuantity()));
//                table.addCell(String.valueOf(decimalFormat.format(sale.getSubtotal())));
//            }
//
//            // Menambahkan tabel ke dokumen
//            document.add(table);
//
//            // Menutup dokumen
//            document.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

