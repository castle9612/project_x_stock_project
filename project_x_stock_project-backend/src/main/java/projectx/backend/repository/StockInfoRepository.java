package projectx.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectx.backend.entity.StockInfo;

import java.util.List;
import java.util.Optional;

/**
 * 주식 정보에 대한 데이터베이스 작업을 처리하는 레포지토리 인터페이스
 */
@Repository
public interface StockInfoRepository extends JpaRepository<StockInfo, Long> {
    /**
     * 종목 코드로 주식 정보를 찾음
     * @param stockCode 찾을 주식의 종목 코드
     * @return 해당 종목 코드의 주식 정보 (Optional)
     */
    Optional<StockInfo> findByStockCode(String stockCode);

    /**
     * 회사명에 특정 문자열이 포함된 모든 주식 정보를 찾음
     * @param companyName 검색할 회사명
     * @return 검색된 주식 정보 리스트
     */
    List<StockInfo> findByCompanyContaining(String companyName);

    /**
     * 특정 종목 코드가 존재하는지 확인
     * @param stockCode 확인할 종목 코드
     * @return 존재 여부 (boolean)
     */
    boolean existsByStockCode(String stockCode);
}