package comp.finalproject.admin.repository;

import comp.finalproject.admin.entity.Item;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Repository
@JaversSpringDataAuditable
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAll();

    Item findById(long id);

    Item findByName(String name);

    Item findByDeletedAt(Date date);
    List<Item> findByTotalSold(int totalSold);
    List<Item> findByTotalSoldGreaterThanOrderByTotalSoldDesc(int totalSold);

    List<Item> findByDeletedFalseOrderByIdDesc();
    List<Item> findByDeletedTrueOrderByIdDesc();
    List<Item> findByDeletedFalseOrderByCreatedAtDesc();
    List<Item> findByDeletedFalseAndTotalSoldGreaterThanOrderByTotalSoldDesc(int totalSold);

    /*void deleteById(long id);


    void update(Item item);

    int deleteById(int id);

    void save(Item item);

    */

}
