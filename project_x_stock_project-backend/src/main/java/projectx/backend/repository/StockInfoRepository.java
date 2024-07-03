package projectx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectx.backend.entity.StockInfo;

import java.util.List;
import java.util.Optional;


@Repository
public interface StockInfoRepository extends JpaRepository<StockInfo, Long> {

    Optional<StockInfo> findByStockCode(String stockCode);


    List<StockInfo> findByCompanyContaining(String companyName);


    boolean existsByStockCode(String stockCode);
}