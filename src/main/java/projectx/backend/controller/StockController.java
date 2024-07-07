package projectx.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import projectx.backend.dto.StockGraphDto;
import projectx.backend.service.StockService;

import java.time.LocalDate;
import java.util.List;

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

    @PostMapping("/stock-data-for-graph")
    public List<StockGraphDto> stockGraphData(
            @RequestParam String stockCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return stockService.getStockGraphData(stockCode, startDate, endDate);

    }


}
