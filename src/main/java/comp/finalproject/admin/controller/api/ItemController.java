package comp.finalproject.admin.controller.api;

import comp.finalproject.admin.entity.Item;
import comp.finalproject.admin.model.ItemRequest;
import comp.finalproject.admin.service.api.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Component("apiItemController")
@RequestMapping("/api")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping("/items")
    public List<Item> findAll() {
        return itemService.findAll();
    }

    @GetMapping("/item/{id}")
    public Item getFindById(@PathVariable("id") long id) {
        return itemService.findById(id);
    }

    @PostMapping("/item")
    public ResponseEntity<?> create(@RequestBody ItemRequest item) {
        return itemService.createRequest(item);
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<?> edit(@RequestBody ItemRequest item, @PathVariable long id) {
        return itemService.editRequest(id, item);
    }

    @DeleteMapping("/makanan/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        return itemService.softDeleteByIdApi(id);
    }

    @PostMapping("/makanan/restore/{id}")
    public ResponseEntity<?> restoreById(@PathVariable long id) {
        return itemService.restoreById(id);
    }

}
