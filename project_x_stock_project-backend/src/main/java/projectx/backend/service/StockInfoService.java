package projectx.backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectx.backend.entity.StockInfo;
import projectx.backend.repository.StockInfoRepository;

import java.util.List;
import java.util.Optional;

/**
 * 주식 정보 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
public class StockInfoService {
    private final StockInfoRepository stockInfoRepository;

    @Autowired
    public StockInfoService(StockInfoRepository stockInfoRepository) {
        this.stockInfoRepository = stockInfoRepository;
    }

    /**
     * 모든 주식 정보를 페이지 단위로 조회
     * @param pageable 페이지 정보
     * @return 페이지 단위의 주식 정보
     */
    @Transactional(readOnly = true)
    public Page<StockInfo> getAllStocks(Pageable pageable) {
        return stockInfoRepository.findAll(pageable);
    }

    /**
     * 특정 종목 코드로 주식 정보를 조회
     * @param stockCode 조회할 주식의 종목 코드
     * @return 해당 종목 코드의 주식 정보 (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<StockInfo> getStockByCode(String stockCode) {
        return stockInfoRepository.findByStockCode(stockCode);
    }

    /**
     * 새로운 주식 정보를 저장
     * @param stockInfo 저장할 주식 정보
     * @return 저장된 주식 정보
     */
    @Transactional
    public StockInfo saveStock(StockInfo stockInfo) {
        return stockInfoRepository.save(stockInfo);
    }

    /**
     * 기존 주식 정보를 업데이트
     * @param stockCode 업데이트할 주식의 종목 코드
     * @param updatedStock 업데이트할 주식 정보
     * @return 업데이트된 주식 정보 (Optional)
     */
    @Transactional
    public Optional<StockInfo> updateStock(String stockCode, StockInfo updatedStock) {
        return stockInfoRepository.findByStockCode(stockCode)
                .map(stock -> {
                    // 필드 업데이트 코드...
                    return stockInfoRepository.save(stock);
                });
    }

    /**
     * 주식 정보를 삭제
     * @param stockCode 삭제할 주식의 종목 코드
     * @throws EntityNotFoundException 해당 종목 코드의 주식 정보가 없을 경우
     */
    @Transactional
    public void deleteStock(String stockCode) {
        StockInfo stockInfo = stockInfoRepository.findByStockCode(stockCode)
            .orElseThrow(() -> new EntityNotFoundException("Stock not found with code: " + stockCode));
        stockInfoRepository.delete(stockInfo);
    }

    /**
     * 회사명으로 주식 정보를 검색
     * @param companyName 검색할 회사명
     * @return 검색된 주식 정보 리스트
     */
    @Transactional(readOnly = true)
    public List<StockInfo> searchStocksByCompany(String companyName) {
        return stockInfoRepository.findByCompanyContaining(companyName);
    }
}