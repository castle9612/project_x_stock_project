package projectx.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectx.backend.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
}
