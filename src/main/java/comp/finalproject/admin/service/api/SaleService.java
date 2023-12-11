package comp.finalproject.admin.service.api;

import comp.finalproject.admin.entity.Item;
import comp.finalproject.admin.entity.Sale;
import comp.finalproject.admin.entity.User;
import comp.finalproject.admin.repository.ItemRepository;
import comp.finalproject.admin.repository.SalesRepository;
import comp.finalproject.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Component("apiSaleService")
public class SaleService {
    @Autowired
    private SalesRepository salesRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Sale> findAll() {
        return salesRepository.findByDeletedFalseOrderByIdDesc();
    }

    public ResponseEntity<?> findByUserId(long id) {
        return new ResponseEntity<>(salesRepository.findByUserId(id), HttpStatus.OK);
    }

    public ResponseEntity<?> createSale(long userId, long itemId, int quantity) {
        // Mendapatkan user dan item
        User currentUser = userRepository.findById(userId).orElse(null);
        Item currentItem = itemRepository.findById(itemId);

        if (currentUser != null && currentItem != null) {
            // Hitung subtotal
            float subtotal = currentItem.getAmount() * quantity;
            int getQuantity = currentItem.getQuantity() - quantity;
            currentItem.setQuantity(getQuantity);

            // Objek sale
            Sale sale = new Sale();
            sale.setUser(currentUser);
            sale.setItem(currentItem);
            sale.setQuantity(quantity);
            sale.setSubtotal(subtotal);
            sale.setDate(new Date());
            sale.setStatus("Success");
            sale.setMetodePembayaran("cash");
            sale.setProofOfPayment(null);

            // Simpan penjualan

            // Setelah penjualan disimpan, kirim respons
            Sale savedSale = salesRepository.save(sale); // Ganti dengan metode penyimpanan yang sesuai
            return new ResponseEntity<>(savedSale, HttpStatus.OK);
        } else {
            // Jika user atau item tidak ditemukan, kirim respons error
            return new ResponseEntity<>("User atau Item tidak ditemukan", HttpStatus.NOT_FOUND);
        }
    }
    public ResponseEntity<?> delete(long id) {
        Sale sale = salesRepository.findById(id);
        if (sale != null) {
            sale.setDeleted(true);
            salesRepository.save(sale);
            return new ResponseEntity<>(sale, HttpStatus.OK);
        }
        return new ResponseEntity<>("Sales Tidak ada", HttpStatus.BAD_REQUEST);
    }
}
