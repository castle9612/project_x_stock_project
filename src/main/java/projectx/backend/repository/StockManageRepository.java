package projectx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectx.backend.entity.StockManage;
import projectx.backend.entity.User;
import projectx.backend.entity.StockInfo;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockManageRepository extends JpaRepository<StockManage, Long> {
    List<StockManage> findByUserId(Long userId);
    List<StockManage> findByStockInfoId(Long stockId);
    Optional<StockManage> findByUserAndStockInfo(User user, StockInfo stockInfo);
}