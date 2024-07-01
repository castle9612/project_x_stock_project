package projectx.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectx.backend.entity.StockInfo;
import projectx.backend.service.StockInfoService;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * 주식 정보 관련 API를 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/api/stock-info")
public class StockInfoController {
    private final StockInfoService stockInfoService;

    @Autowired
    public StockInfoController(StockInfoService stockInfoService) {
        this.stockInfoService = stockInfoService;
    }

    /**
     * 모든 주식 정보를 페이지 단위로 조회
     * @param pageable 페이지 정보
     * @return 페이지 단위의 주식 정보
     */
    @GetMapping
    public Page<StockInfo> getAllStocks(Pageable pageable) {
        return stockInfoService.getAllStocks(pageable);
    }

    /**
     * 특정 종목 코드로 주식 정보를 조회
     * @param stockCode 조회할 주식의 종목 코드
     * @return 해당 종목 코드의 주식 정보, 없으면 404 Not Found
     */
    @GetMapping("/{stockCode}")
    public ResponseEntity<StockInfo> getStockByCode(@PathVariable String stockCode) {
        return stockInfoService.getStockByCode(stockCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 새로운 주식 정보를 생성
     * @param stockInfo 생성할 주식 정보
     * @return 생성된 주식 정보
     */
    @PostMapping
    public ResponseEntity<?> createStock(@RequestBody StockInfo stockInfo) {
        if (stockInfo.getStockCode() == null || stockInfo.getStockCode().isEmpty()) {
            return ResponseEntity.badRequest().body("Stock code is required");
        }
        try {
            StockInfo createdStock = stockInfoService.saveStock(stockInfo);
            return ResponseEntity.ok(createdStock);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create stock: " + e.getMessage());
        }
    }

    /**
     * 기존 주식 정보를 업데이트
     * @param stockCode 업데이트할 주식의 종목 코드
     * @param stockInfo 업데이트할 주식 정보
     * @return 업데이트된 주식 정보, 없으면 404 Not Found
     */
    @PutMapping("/{stockCode}")
    public ResponseEntity<?> updateStock(@PathVariable String stockCode, @RequestBody StockInfo stockInfo) {
        if (stockInfo.getStockCode() == null || !stockInfo.getStockCode().equals(stockCode)) {
            return ResponseEntity.badRequest().body("Stock code mismatch");
        }
        return stockInfoService.updateStock(stockCode, stockInfo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 주식 정보를 삭제
     * @param stockCode 삭제할 주식의 종목 코드
     * @return 삭제 성공 시 200 OK, 주식 정보가 없으면 404 Not Found
     */
    @DeleteMapping("/{stockCode}")
    public ResponseEntity<Void> deleteStock(@PathVariable String stockCode) {
        try {
            stockInfoService.deleteStock(stockCode);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 회사명으로 주식 정보를 검색
     * @param companyName 검색할 회사명
     * @return 검색된 주식 정보 리스트
     */
    @GetMapping("/search")
    public List<StockInfo> searchStocksByCompany(@RequestParam String companyName) {
        return stockInfoService.searchStocksByCompany(companyName);
    }
}