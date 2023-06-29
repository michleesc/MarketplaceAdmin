package comp.finalproject.admin.repository;

import comp.finalproject.admin.entity.Item;
import comp.finalproject.admin.entity.Role;
import net.bytebuddy.asm.Advice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAll();

    Item findById(long id);

    List<Item> findByTotalSold(int totalSold);
    List<Item> findByTotalSoldGreaterThanOrderByTotalSoldDesc(int totalSold);

    /*void deleteById(long id);


    void update(Item item);

    int deleteById(int id);

    void save(Item item);

    */

}
