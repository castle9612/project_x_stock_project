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
    public StockManage buyStock(Long userId, Long stockId, int money) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        StockInfo stockInfo = stockInfoRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        // 사용자의 현금이 충분한지 확인
        if (user.getCash() < money) {
            throw new RuntimeException("Insufficient funds");
        }

        // 현재 주가로 구매 가능한 주식 수량 계산
        int currentPrice = stockInfo.getCurrentPrice();
        int quantity = money / currentPrice;

        // 사용자의 현금 차감
        user.setCash(user.getCash() - money);
        userRepository.save(user);

        // StockManage 엔티티 생성 또는 업데이트
        StockManage stockManage = stockManageRepository.findByUserAndStockInfo(user, stockInfo)
        	    .orElse(new StockManage());
        	if (stockManage.getId() == null) {
        	    stockManage.setUser(user);
        	    stockManage.setStockInfo(stockInfo);
        	    stockManage.setMoney(0);
        	    stockManage.setQuantity(0);
        	}
        	stockManage.setMoney(stockManage.getMoney() + money);
        	stockManage.setQuantity(stockManage.getQuantity() + quantity);

        // 수익률 계산 (예: (현재가치 - 매수금액) / 매수금액 * 100)
        int currentValue = currentPrice * stockManage.getQuantity();
        double earningsRate = ((double)(currentValue - stockManage.getMoney()) / stockManage.getMoney()) * 100;
        stockManage.setEarningsRate(earningsRate);

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
        user.setCash(user.getCash() + saleMoney);
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
            int currentValue = currentPrice * stockManage.getQuantity();
            double earningsRate = ((double)(currentValue - stockManage.getMoney()) / stockManage.getMoney()) * 100;
            stockManage.setEarningsRate(earningsRate);
            return stockManageRepository.save(stockManage);
        }
    }
}