package comp.finalproject.admin.repository;

import comp.finalproject.admin.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface SalesRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByUserId(long userId);
    Sale findById(long id);
    List<Sale> findAllByOrderByIdDesc();
    List<Sale> findByDateBetween(Date startDate, Date endDate);
    List<Sale> findByDateBetweenOrderByIdDesc(Date startDate, Date endDate);
    List<Sale> findByDeletedFalseOrderByIdDesc();
    List <Sale> findByDeletedFalseAndDateBetweenOrderByIdDesc(Date startDate, Date endDate);
}
