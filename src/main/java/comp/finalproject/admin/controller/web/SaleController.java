package comp.finalproject.admin.controller.web;

import comp.finalproject.admin.entity.Item;
import comp.finalproject.admin.entity.Sale;
import comp.finalproject.admin.entity.User;
import comp.finalproject.admin.repository.ItemRepository;
import comp.finalproject.admin.repository.SalesRepository;
import comp.finalproject.admin.service.web.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
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

        List<Sale> allSales = salesRepository.findAll();
        model.addAttribute("sales", allSales);

        List<String> statusList = Arrays.asList("Menunggu", "Success");
        model.addAttribute("statusList", statusList);

        return "sale/sales";
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
}

