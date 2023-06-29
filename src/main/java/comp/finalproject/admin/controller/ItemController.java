package comp.finalproject.admin.controller;

import comp.finalproject.admin.entity.Item;
import comp.finalproject.admin.entity.User;
import comp.finalproject.admin.repository.ItemRepository;
import comp.finalproject.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class ItemController {

    @Autowired
    private UserService userService;
    @Autowired
    private ItemRepository itemRepository;

    // handler methods go here...
    @RequestMapping("/items")
    public String viewHomePage(Model model, Principal principal) {
        String email = principal.getName();

        User user = userService.findUserByEmail(email);
        String name = user.getName();

        // Menambahkan atribut firstName ke objek Model
        model.addAttribute("email", email);
        model.addAttribute("name", name);

        List<Item> listItem = itemRepository.findAll();
        model.addAttribute("listItem", listItem);
        return "item/items";
    }

    @RequestMapping("/newitem")
    public String showNewForm(Model model) {
        Item item = new Item();
        model.addAttribute("item", item);

        return "item/itemsnew";
    }

    @RequestMapping(value = "/saveitem", method = RequestMethod.POST)
    public String saveItem(@ModelAttribute("item") Item item, @RequestParam("image") MultipartFile image) throws IOException, IOException {
        if (!image.isEmpty()) {
            // Simpan gambar ke server atau lakukan operasi lain sesuai kebutuhan Anda
            String fileName = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();

            // Mengupload file ke admin
//            String uploadDirAdmin = "C:\\Users\\ASUS\\TIA - Academy\\MerdekaFinalProjectMarketplaceAdmin\\MerdekaFinalProjectMarketplaceAdmin\\src\\main\\resources\\static\\image\\item"; // Ubah dengan direktori upload yang sesuai di server Anda

            // Mengupload file ke user
            String uploadDirUser = "C:\\Users\\ASUS\\TIA - Academy\\MerdekaFinalProjectMarketplaceUser\\MerdekaFinalProjectMarketplaceUser\\src\\main\\resources\\static\\image\\item";

            // Mengupload path ke database
            String uploadDatabase = "/image/item/";

            String filePathUser = Paths.get(uploadDirUser, fileName).toString(); // user
            String filePathDatabase = Paths.get(uploadDatabase, fileName).toString(); // database
//            String filePathAdmin = Paths.get(uploadDirAdmin, fileName).toString(); // admin

            // Simpan gambar ke server
//            image.transferTo(new File(filePathAdmin));
            image.transferTo(new File(filePathUser));

            // Setel path gambar ke model
            item.setImagePath(filePathDatabase);
        }

        itemRepository.save(item);

        return "redirect:/items";
    }


    @RequestMapping("/itemsedit/{id}")
    public String showEditForm(@PathVariable(name = "id") long id, Model model) {
        Item item = itemRepository.findById(id);
        if (item != null) {
            model.addAttribute("item", item);
            return "item/itemsedit";
        } else {
            return "error";
        }
    }

    @RequestMapping(value = "/updateitem", method = RequestMethod.POST)
    public String updateItem(@ModelAttribute("item") Item item, @RequestParam("image") MultipartFile image) throws IOException {
        if (!image.isEmpty()) {
            // Hapus gambar lama jika ada
            if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
                // File Path Admin
                String oldFilePathAdmin = "C:\\Users\\ASUS\\TIA - Academy\\MerdekaFinalProjectMarketplaceAdmin\\MerdekaFinalProjectMarketplaceAdmin\\src\\main\\resources\\static" + item.getImagePath(); // Ganti dengan path gambar lama yang sesuai di server Anda
                File oldFileAdmin = new File(oldFilePathAdmin);
                // File Path User
                String oldFilePathUser = "C:\\Users\\ASUS\\TIA - Academy\\MerdekaFinalProjectMarketplaceUser\\MerdekaFinalProjectMarketplaceUser\\src\\main\\resources\\static" + item.getImagePath();
                File oldFileUser = new File(oldFilePathUser);
                // DELETE
                oldFileAdmin.delete();
                oldFileUser.delete();
            }
            // Simpan gambar ke server atau lakukan operasi lain sesuai kebutuhan Anda
            String fileName = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();

            String uploadDirUser = "C:\\Users\\ASUS\\TIA - Academy\\MerdekaFinalProjectMarketplaceUser\\MerdekaFinalProjectMarketplaceUser\\src\\main\\resources\\static\\image\\item";
            String uploadDirAdmin = "C:\\Users\\ASUS\\TIA - Academy\\MerdekaFinalProjectMarketplaceAdmin\\MerdekaFinalProjectMarketplaceAdmin\\src\\main\\resources\\static\\image\\item";
            String uploadDatabase = "/image/item/"; // Ubah dengan direktori upload yang sesuai di server Anda

            String filePathUser = Paths.get(uploadDirUser, fileName).toString(); // user
            String filePathAdmin = Paths.get(uploadDirAdmin, fileName).toString(); // admin
            String filePathDatabase = Paths.get(uploadDatabase, fileName).toString(); // database

            // Simpan gambar ke server
            image.transferTo(new File(filePathAdmin));
            image.transferTo(new File(filePathUser));

            // Setel path gambar ke model
            item.setImagePath(filePathDatabase);
        }

        itemRepository.save(item);

        return "redirect:/items";
    }

    @RequestMapping("/deleteitem/{id}")
    public String delete(@PathVariable(name = "id") long id) {
        itemRepository.deleteById(id);
        return "redirect:/items";
    }

}
