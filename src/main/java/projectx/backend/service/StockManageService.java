package projectx.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectx.backend.repository.StockManageRepository;
import projectx.backend.repository.UserRepository;
import projectx.backend.repository.StockInfoRepository;
import projectx.backend.entity.StockManage;
import projectx.backend.entity.User;
import projectx.backend.entity.StockInfo;
import java.util.List;
import java.util.Optional;


@Service
public class StockManageService {
    private final StockManageRepository stockManageRepository;
    private final UserRepository userRepository;
    private final StockInfoRepository stockInfoRepository;

    @Autowired
    public StockManageService(StockManageRepository stockManageRepository,
                              UserRepository userRepository,
                              StockInfoRepository stockInfoRepository) {
        this.stockManageRepository = stockManageRepository;
        this.userRepository = userRepository;
        this.stockInfoRepository = stockInfoRepository;
    }

    public List<StockManage> getStockManageByUserId(Long userId) {
        return stockManageRepository.findByUserId(userId);
    }
    
    @Transactional
    public double updateAndGetEarningsRate(Long userId, Long stockId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        StockInfo stockInfo = stockInfoRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        
        StockManage stockManage = stockManageRepository.findByUserAndStockInfo(user, stockInfo)
                .orElseThrow(() -> new RuntimeException("Stock not owned by user"));

        return updateEarningsRate(stockManage);
    }

    @Transactional
    public double updateEarningsRate(StockManage stockManage) {
        StockInfo stockInfo = stockManage.getStockInfo();
        int currentPrice = stockInfo.getCurrentPrice();
        int currentValue = currentPrice * stockManage.getQuantity();
        double earningsRate = ((double)(currentValue - stockManage.getMoney()) / stockManage.getMoney()) * 100;
        stockManage.setEarningsRate(earningsRate);
        stockManageRepository.save(stockManage);
        return earningsRate;
    }

    // 모든 주식의 수익률 업데이트. 어디 부분에서 어떻게 쓸까? 사용 안할수도.
    @Transactional
    public void updateAllEarningsRates() {
        List<StockManage> allStockManages = stockManageRepository.findAll();
        for (StockManage stockManage : allStockManages) {
            updateEarningsRate(stockManage);
        }
    }

    @Transactional
    public StockManage buyStock(Long userId, Long stockId, int money) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        StockInfo stockInfo = stockInfoRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        // 사용자의 현금이 충분한지 확인
        if (user.getCurrentMoney() < money) {
            throw new RuntimeException("Insufficient funds");
        }

        // 현재 주가로 구매 가능한 주식 수량 계산
        int currentPrice = stockInfo.getCurrentPrice();
        int quantity = money / currentPrice;

        // 사용자의 현금 차감
        user.setCurrentMoney(user.getCurrentMoney() - money);
        userRepository.save(user);
        	
        // StockManage 엔티티 생성 또는 업데이트
        Optional<StockManage> existingStockManage = stockManageRepository.findByUserAndStockInfo(user, stockInfo);
            
        StockManage stockManage;
        if (existingStockManage.isPresent()) {
            // 이미 해당 주식을 보유하고 있는 경우
            stockManage = existingStockManage.get();
            stockManage.setMoney(stockManage.getMoney() + money);
            stockManage.setQuantity(stockManage.getQuantity() + quantity);
        } else {
            // 새로 주식을 구매하는 경우
            stockManage = new StockManage();
            stockManage.setUser(user);
            stockManage.setStockInfo(stockInfo);
            stockManage.setMoney(money);
            stockManage.setQuantity(quantity);
        }
        
        updateEarningsRate(stockManage);

        return stockManageRepository.save(stockManage);
    }

    @Transactional
    public StockManage sellStock(Long userId, Long stockId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        StockInfo stockInfo = stockInfoRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        StockManage stockManage = stockManageRepository.findByUserAndStockInfo(user, stockInfo)
                .orElseThrow(() -> new RuntimeException("Stock not owned"));

        // 판매 가능한 수량인지 확인
        if (stockManage.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock quantity");
        }

        int currentPrice = stockInfo.getCurrentPrice();
        int saleMoney = currentPrice * quantity;

        // 사용자의 현금 증가
        user.setCurrentMoney(user.getCurrentMoney() + saleMoney);
        userRepository.save(user);

        // StockManage 엔티티 업데이트
        stockManage.setQuantity(stockManage.getQuantity() - quantity);
        stockManage.setMoney(stockManage.getMoney() - saleMoney);

        // 모든 주식을 판매한 경우 StockManage 엔티티 삭제
        if (stockManage.getQuantity() == 0) {
            stockManageRepository.delete(stockManage);
            return null;
        } else {
            // 수익률 재계산
            updateEarningsRate(stockManage);
            return stockManageRepository.save(stockManage);
        }
    }
}