package comp.finalproject.admin.service.web;

import comp.finalproject.admin.entity.Item;
import comp.finalproject.admin.repository.ItemRepository;
import org.javers.core.Javers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Component("webItemService")
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    public Item findById(long id) {
        return itemRepository.findById(id);
    }

    public List<Item> findAll() {
        return itemRepository.findByDeletedFalseOrderByIdDesc();
    }

    public void saveItemImage(Item item, MultipartFile image) throws IOException {
        if (!image.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
            String uploadDirUser = "C:\\Users\\ASUS\\OneDrive - Microsoft 365\\Documents\\TIA - Academy\\MerdekaFinalProjectMarketplaceUser\\MerdekaFinalProjectMarketplaceUser\\src\\main\\resources\\static\\image\\item";
            String uploadDatabase = "/image/item/";

            String filePathUser = Paths.get(uploadDirUser, fileName).toString();
            String filePathDatabase = Paths.get(uploadDatabase, fileName).toString();

            image.transferTo(new File(filePathUser));
            item.setImagePath(filePathDatabase);

        }
        item.setCreatedAt(new Date());
        itemRepository.save(item);
    }

    public void updateItemImage(Item item, MultipartFile image) throws IOException {
        String imagePath = "C:\\Users\\ASUS\\OneDrive - Microsoft 365\\Documents\\TIA - Academy\\MerdekaFinalProjectMarketplaceUser\\MerdekaFinalProjectMarketplaceUser\\src\\main\\resources\\static\\image\\item";
        if (!image.isEmpty()) {
            if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
                String imageBefore = "C:\\Users\\ASUS\\OneDrive - Microsoft 365\\Documents\\TIA - Academy\\MerdekaFinalProjectMarketplaceUser\\MerdekaFinalProjectMarketplaceUser\\src\\main\\resources\\static";
                Path imagesPathBefore = Paths.get(imageBefore, item.getImagePath());
                try {
                    Files.delete(imagesPathBefore);
                    System.out.println("File "
                            + imagesPathBefore.toAbsolutePath().toString()
                            + " successfully removed");
                } catch (IOException e) {
                    System.err.println("Unable to delete "
                            + imagesPathBefore.toAbsolutePath().toString()
                            + " due to...");
                    e.printStackTrace();
                }
            }
            // Simpan gambar ke server atau lakukan operasi lain sesuai kebutuhan Anda
            String fileName = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();

            String uploadDatabase = "/image/item/"; // Ubah dengan direktori upload yang sesuai di server Anda

            String filePathUser = Paths.get(imagePath, fileName).toString(); // user
            String filePathDatabase = Paths.get(uploadDatabase, fileName).toString(); // database

            // Simpan gambar ke server
            image.transferTo(new File(filePathUser));

            // Setel path gambar ke model
            item.setImagePath(filePathDatabase);
            item.setUpdatedAt(new Date());
        }
        item.setUpdatedAt(new Date());
        // Simpan gambar baru ke server
        itemRepository.save(item);
    }

    public void softDeleteById(long id) {
        Item item = itemRepository.findById(id);
        // jika makanan ada akan diproses
        if (item != null) {
            // setting delete menjadi true
            item.setDeleted(true);
            item.setDeletedAt(new Date());
            item.setUpdatedAt(new Date());
            // simpan perubahan kedalam database
            itemRepository.save(item);
        }
    }
}
