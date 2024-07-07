package projectx.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import projectx.backend.service.StockService;

@RestController
public class StockController {

    @Autowired
    public StockService stockService;

    // DB에 주식 정보 저장하는 코드
    @PostMapping("/fetch-stock-data")
    public String fetchStockData(
            @RequestParam String marketCode,
            @RequestParam String stockCode, // SK하이닉스 000660, 삼성전자: 005930
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String periodCode) {
        stockService.fetchAndSaveStockData(marketCode, stockCode, startDate, endDate, periodCode);
        return stockCode+" Stock data fetched and saved successfully";
    }
}
