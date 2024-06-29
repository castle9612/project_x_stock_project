package projectx.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projectx.backend.service.StockManageService;
import projectx.backend.entity.StockManage;
import java.util.List;

@RestController
@RequestMapping("/api/stock-manage")
public class StockManageController {
    private final StockManageService stockManageService;

    @Autowired
    public StockManageController(StockManageService stockManageService) {
        this.stockManageService = stockManageService;
    }

    @GetMapping("/user/{userId}")
    public List<StockManage> getStockManageByUserId(@PathVariable Long userId) {
        return stockManageService.getStockManageByUserId(userId);
    }

    @PostMapping("/buy")
    public StockManage buyStock(@RequestParam Long userId, @RequestParam Long stockId, @RequestParam int money) {
        return stockManageService.buyStock(userId, stockId, money);
    }

    @PostMapping("/sell")
    public StockManage sellStock(@RequestParam Long userId, @RequestParam Long stockId, @RequestParam int quantity) {
        return stockManageService.sellStock(userId, stockId, quantity);
    }
    
    @GetMapping("/update-earnings-rate")
    public double updateEarningsRate(@RequestParam Long userId, @RequestParam Long stockId) {
        return stockManageService.updateAndGetEarningsRate(userId, stockId);
    }

    // 추가 엔드포인트 구현...
}
