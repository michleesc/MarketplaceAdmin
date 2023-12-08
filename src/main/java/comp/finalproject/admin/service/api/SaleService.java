package comp.finalproject.admin.service.api;

import comp.finalproject.admin.entity.Sale;
import comp.finalproject.admin.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component("apiSaleService")
public class SaleService {
    @Autowired
    private SalesRepository salesRepository;

    public List<Sale> findAll() {
        return salesRepository.findByDeletedFalseOrderByIdDesc();
    }
}
