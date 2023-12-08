package comp.finalproject.admin.controller.api;

import comp.finalproject.admin.entity.Sale;
import comp.finalproject.admin.service.api.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
