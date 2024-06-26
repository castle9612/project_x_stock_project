package projectx.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import projectx.backend.service.StockService;

@RestController
public class StockController {

    @Autowired
    public StockService stockService;

    @PostMapping("/fetch-stock-data")
    public String fetchStockData(
            @RequestParam String marketCode,
            @RequestParam String stockCode,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String periodCode) {
        stockService.fetchAndSaveStockData(marketCode, stockCode, startDate, endDate, periodCode);
        return "Stock data fetched and saved successfully";
    }
}
