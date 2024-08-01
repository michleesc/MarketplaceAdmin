package comp.finalproject.admin.controller.web;

import comp.finalproject.admin.entity.Item;
import comp.finalproject.admin.entity.User;
import comp.finalproject.admin.service.web.ItemService;
import comp.finalproject.admin.service.web.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@Component("webItemController")
public class ItemController {

    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    // handler methods go here...
    @RequestMapping("/items")
    public String viewHomePage(Model model, Principal principal) {
        String email = principal.getName();

        User user = userService.findUserByEmail(email);
        String name = user.getName();

        // Menambahkan atribut firstName ke objek Model
        model.addAttribute("email", email);
        model.addAttribute("name", name);

        List<Item> listItem = itemService.findAll();

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
    public String saveItem(@ModelAttribute("item") Item item, @RequestParam("image") MultipartFile image) throws IOException {
        itemService.saveItemImage(item, image);
        return "redirect:/items";
    }


    @RequestMapping("/itemsedit/{id}")
    public String showEditForm(@PathVariable(name = "id") long id, Model model) {
        Item item = itemService.findById(id);
        if (item != null) {
            model.addAttribute("item", item);
            return "item/itemsedit";
        } else {
            return "error";
        }
    }

    @RequestMapping(value = "/updateitem", method = RequestMethod.POST)
    public String updateItem(@ModelAttribute("item") Item item, @RequestParam("image") MultipartFile image) throws IOException {
        itemService.updateItemImage(item, image);
        return "redirect:/items";
    }

    @RequestMapping("/deleteitem/{id}")
    public String delete(@PathVariable(name = "id") long id) {
        itemService.softDeleteById(id);
        return "redirect:/items";
    }


    @RequestMapping("/restores")
    public String restore(Model model, Principal principal) {

        String email = principal.getName();

        User user = userService.findUserByEmail(email);
        String name = user.getName();

        // Menambahkan atribut firstName ke objek Model
        model.addAttribute("email", email);
        model.addAttribute("name", name);

        List<Item> listItem = itemService.findAllRestore();

        model.addAttribute("listItem", listItem);
        return "item/restores";
    }

    @RequestMapping("/restores/{id}")
    public String restore(@PathVariable(name = "id") long id) {
        itemService.restoreById(id);
        return "redirect:/restores";
    }
}