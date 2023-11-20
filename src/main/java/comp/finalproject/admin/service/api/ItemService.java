package comp.finalproject.admin.service.api;

import comp.finalproject.admin.entity.Item;
import comp.finalproject.admin.model.ItemRequest;
import comp.finalproject.admin.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Component("apiItemService")
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    public Item findById(long id) {
        return itemRepository.findById(id);
    }

    public List<Item> findAll() {
        return itemRepository.findByDeletedFalseOrderByCreatedAtDesc();
    }

    public ResponseEntity<?> createRequest(ItemRequest itemDto) {
        Item item = new Item();

        // Membuat atribute Makanan sesuai data yang diterima
        item.setName(itemDto.getName());
        item.setType(itemDto.getType());
        item.setQuantity(itemDto.getQuantity());
        item.setAmount(itemDto.getAmount());
        item.setTotalSold(itemDto.getTotalSold());
        item.setImagePath(itemDto.getImagePath());
        item.setDescription(itemDto.getDescription());

        // simpan perubahan kedalam database
        Item itemSaved = itemRepository.save(item);

        return new ResponseEntity<>(itemSaved, HttpStatus.CREATED);
    }

    public ResponseEntity<?> editRequest(long id, ItemRequest itemDto) {
        // Temukan objek Makanan berdasarkan ID
        Item item = itemRepository.findById(id);

        // Jika makanan tidak ditemukan, kembalikan respons 404
        if (item == null) {
            return new ResponseEntity<>("Item tidak ada", HttpStatus.NOT_FOUND);
        }

//        Javers javers = JaversBuilder.javers()
//                .build();

        // Salin objek Item sebelum perubahan sebagai "left"
//        Item itemleft = javers.getJsonConverter().fromJson(javers.getJsonConverter().toJson(item), Item.class);

        // Update atribut Makanan sesuai dengan data yang diterima
        item.setName(itemDto.getName());
        item.setType(itemDto.getType());
        item.setQuantity(itemDto.getQuantity());
        item.setAmount(itemDto.getAmount());
        item.setTotalSold(itemDto.getTotalSold());
        item.setImagePath(itemDto.getImagePath());
        item.setDescription(itemDto.getDescription());

        // Simpan perubahan ke dalam database
        Item editItem = itemRepository.save(item);

        // Bandingkan Makanan sebelum dan sesudah perubahan
//        Diff diff = javers.compare(itemLeft, editItem);

//        System.out.println(diff.prettyPrint());

        return new ResponseEntity<>(editItem, HttpStatus.OK);
    }

    public ResponseEntity<?> softDeleteByIdApi(long id) {
        Item item = itemRepository.findById(id);
        // jika makanan ada akan diproses
        if (item != null) {
            // setting delete menjadi true
            item.setDeleted(true);
            item.setDeletedAt(new Date());
            // simpan perubahan kedalam database
            itemRepository.save(item);
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }
    public ResponseEntity<?> restoreById(long id) {
        Item item = itemRepository.findById(id);
        // jika makanan ada akan diproses
        if (item != null) {
            // setting delete menjadi true
            item.setDeleted(false);
            item.setDeletedAt(new Date());
            // simpan perubahan kedalam database
            itemRepository.save(item);
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }
}
