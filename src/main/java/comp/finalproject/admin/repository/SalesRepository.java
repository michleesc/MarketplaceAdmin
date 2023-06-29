package comp.finalproject.admin.repository;

import comp.finalproject.admin.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Repository
public interface SalesRepository extends JpaRepository<Sale, Long> {

    List<Sale> findAll();
    List<Sale> findByUserId(long userId);
    Sale findById(long id);
    List<Sale> findByDate(Date date);

    List<Sale> findAllByOrderByIdDesc();

    /*int deleteById(int id);

    void save(Sale sale);

    void update(Sale sale);*/

}
