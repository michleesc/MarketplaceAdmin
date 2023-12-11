package comp.finalproject.admin.controller.api;

import comp.finalproject.admin.entity.Sale;
import comp.finalproject.admin.service.api.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Component("apiSaleController")
@RequestMapping("/api")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping("/sales")
    public List<Sale> findAll() {
        return saleService.findAll();
    }
    @GetMapping("/sales/{userId}")
    public ResponseEntity<?> findByUserId(@PathVariable(name = "userId") long userId) {
        return saleService.findByUserId(userId);
    }
    @PostMapping("/buyItem/{userId}/{itemId}")
    public ResponseEntity<?> buyItem(@PathVariable(name = "userId") long userId, @PathVariable("itemId") long itemId,
                                     @RequestParam("quantity") int quantity) {
        return saleService.createSale(userId, itemId, quantity);
    }
    @DeleteMapping("/sales/delete/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable(name = "id") long id) {
        return saleService.delete(id);
    }
}
