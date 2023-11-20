package comp.finalproject.admin.controller.web;

import comp.finalproject.admin.entity.Item;
import comp.finalproject.admin.entity.Sale;
import comp.finalproject.admin.entity.User;
import comp.finalproject.admin.repository.ItemRepository;
import comp.finalproject.admin.repository.SalesRepository;
import comp.finalproject.admin.repository.UserRepository;
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
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
public class PageController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private SalesRepository salesRepository;

    public PageController(UserService userService, UserRepository userRepository, SalesRepository salesRepository, ItemRepository itemRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.salesRepository = salesRepository;
        this.itemRepository = itemRepository;
    }

    public PageController() {

    }

    @GetMapping("/pages")
    public String pages(Model model, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String name = user.getName();
            model.addAttribute("email", email);
            model.addAttribute("name", name);
        }

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        Date startDate = java.sql.Date.valueOf(today);
        Date endDate = java.sql.Date.valueOf(tomorrow);

        List<Sale> allSales = salesRepository.findByDateBetween(startDate, endDate);
        model.addAttribute("sales", allSales);


        // Top Selling Items
        List<Item> topSellingItems = itemRepository.findByTotalSoldGreaterThanOrderByTotalSoldDesc(0);
        model.addAttribute("topSellingItems", topSellingItems);

        return "page/pages";
    }

    @GetMapping("/pages-profile")
    public String profile(Model model, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        long userId = user.getId();

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
        return "redirect:/pages-profile";
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

        return "redirect:/pages-profile";
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
