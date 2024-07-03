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


@RestController
@RequestMapping("/api/stock-info")
public class StockInfoController {
    private final StockInfoService stockInfoService;

    @Autowired
    public StockInfoController(StockInfoService stockInfoService) {
        this.stockInfoService = stockInfoService;
    }


    @GetMapping
    public Page<StockInfo> getAllStocks(Pageable pageable) {
        return stockInfoService.getAllStocks(pageable);
    }


    @GetMapping("/{stockCode}")
    public ResponseEntity<StockInfo> getStockByCode(@PathVariable String stockCode) {
        return stockInfoService.getStockByCode(stockCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


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


    @PutMapping("/{stockCode}")
    public ResponseEntity<?> updateStock(@PathVariable String stockCode, @RequestBody StockInfo stockInfo) {
        if (stockInfo.getStockCode() == null || !stockInfo.getStockCode().equals(stockCode)) {
            return ResponseEntity.badRequest().body("Stock code mismatch");
        }
        return stockInfoService.updateStock(stockCode, stockInfo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{stockCode}")
    public ResponseEntity<Void> deleteStock(@PathVariable String stockCode) {
        try {
            stockInfoService.deleteStock(stockCode);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/search")
    public List<StockInfo> searchStocksByCompany(@RequestParam String companyName) {
        return stockInfoService.searchStocksByCompany(companyName);
    }
}