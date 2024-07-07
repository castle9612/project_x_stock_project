package projectx.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectx.backend.entity.Stock;


import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findByStockCodeAndDateBetween(String stockCode, LocalDate startDate, LocalDate endDate);
}
