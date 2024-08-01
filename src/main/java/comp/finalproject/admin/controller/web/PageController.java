package comp.finalproject.admin.controller.web;

import comp.finalproject.admin.entity.Item;
import comp.finalproject.admin.entity.Sale;
import comp.finalproject.admin.entity.User;
import comp.finalproject.admin.repository.UserRepository;
import comp.finalproject.admin.service.web.ItemService;
import comp.finalproject.admin.service.web.PageService;
import comp.finalproject.admin.service.web.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class PageController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private PageService pageService;

    @GetMapping("/pages")
    public String pages(Model model, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String name = user.getName();
            model.addAttribute("email", email);
            model.addAttribute("name", name);
        }
        // mendapatkan sales hari ini
        List<Sale> allSales = pageService.getCurrentSales();

        // Menghitung jumlah penjualan sebelumnya dan hari ini menggunakan metode di service
        int currentSalesCount = pageService.getCurrentSalesCount();

        // Menghitung total dari subtotal penjualan hari ini
        double currentTotal = allSales.stream().mapToDouble(Sale::getSubtotal).sum();

        // Menghitung persentase perubahan
        double percentageChangeOmset = pageService.calculatePercentageChangeYesterday();
        double salesComparisonPercentage = pageService.calculateSalesComparisonPercentage();

        // Pembulatan persentase menjadi satu desimal
        double roundedPercentageChangeOmset = Math.round(percentageChangeOmset * 10.0) / 10.0;
        double roundedSalesComparisonPercentage = Math.round(salesComparisonPercentage * 10.0) / 10.0;

        List<Item> topSellingItems = itemService.topSellingItems();

        // Total Omset Today
        model.addAttribute("omsetToday", currentTotal);
        // Percentage Omset Today
        model.addAttribute("percentageChangeOmset", roundedPercentageChangeOmset);
        // Total Sales Today
        model.addAttribute("salesToday", currentSalesCount);
        // Percentage Sales Today
        model.addAttribute("salesComparisonPercentage", roundedSalesComparisonPercentage);
        // Top Selling Items
        model.addAttribute("topSellingItems", topSellingItems);
        // All Sales Today
        model.addAttribute("sales", allSales);

        return "page/pages";
    }

    @GetMapping("/pages-profile")
    public String profile(Model model, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        if (user != null) {
            String name = user.getName();
            model.addAttribute("email", email);
            model.addAttribute("name", name);
        }

        return "page/pages-profile";
    }

    @GetMapping("/pages-profile/edit")
    public String editProfile(Model model, Principal principal) {
        String email = principal.getName();

        User user = userService.findUserByEmail(email);
        String name = user.getName();

        // Menambahkan atribut firstName ke objek Model
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        model.addAttribute("user", user);
        return "page/edit-profile";
    }

    @PostMapping("/pages-profile/save")
    public String saveProfile(Principal principal, @ModelAttribute User user) {
        String email = principal.getName();
        User existingUser = userRepository.findByEmail(email);

        if (user.getName() != null && !user.getName().isEmpty()) {
            existingUser.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            existingUser.setEmail(user.getEmail());
        }
        userRepository.save(existingUser);
        return "redirect:/logout";
    }

    @PostMapping("/pages-profile/passwordsave")
    public String savePassword(@RequestParam("password") String currentPassword,
                               @RequestParam("newpassword") String newPassword,
                               @RequestParam("renewpassword") String reEnterNewPassword,
                               Principal principal,
                               Model model) {

        String email = principal.getName();
        User existingUser = userRepository.findByEmail(email);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder);

        // Validasi password saat ini
        if (!passwordEncoder.matches(currentPassword, existingUser.getPassword())) {
            model.addAttribute("passwordError", "Current password is incorrect");
            return "redirect:/pages-profile";
        }

        // Validasi password baru
        if (!newPassword.equals(reEnterNewPassword)) {
            model.addAttribute("passwordError", "New password and re-entered password do not match");
            return "redirect:/pages-profile";
        }

        // Setel password baru
        existingUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(existingUser);

        return "redirect:/logout";
    }

    @GetMapping("/pages-faq")
    public String faq(Model model, Principal principal) {

        String email = principal.getName();

        User user = userService.findUserByEmail(email);
        String name = user.getName();

        // Menambahkan atribut firstName ke objek Model
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        return "page/pages-faq";
    }
}