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


@Service
public class StockInfoService {
    private final StockInfoRepository stockInfoRepository;

    @Autowired
    public StockInfoService(StockInfoRepository stockInfoRepository) {
        this.stockInfoRepository = stockInfoRepository;
    }


    @Transactional(readOnly = true)
    public Page<StockInfo> getAllStocks(Pageable pageable) {
        return stockInfoRepository.findAll(pageable);
    }


    @Transactional(readOnly = true)
    public Optional<StockInfo> getStockByCode(String stockCode) {
        return stockInfoRepository.findByStockCode(stockCode);
    }


    @Transactional
    public StockInfo saveStock(StockInfo stockInfo) {
        return stockInfoRepository.save(stockInfo);
    }

    @Transactional
    public Optional<StockInfo> updateCurrentPrice(String stockCode, double updatePrice) {
        return stockInfoRepository.findByStockCode(stockCode)
                .map(stock -> {
                    stock.setCurrentPrice(updatePrice);
                    return stockInfoRepository.save(stock);
                });
    }


    @Transactional
    public Optional<StockInfo> updateStock(String stockCode, StockInfo updatedStock) {
        return stockInfoRepository.findByStockCode(stockCode)
                .map(stock -> {
                    stock.setStockList(updatedStock.getStockList());
                    stock.setCompany(updatedStock.getCompany());
                    stock.setStockType(updatedStock.getStockType());
                    stock.setMarketCapitalization(updatedStock.getMarketCapitalization());
                    stock.setDividend(updatedStock.getDividend());
                    stock.setCurrentPrice(updatedStock.getCurrentPrice());
                    stock.setVolume(updatedStock.getVolume());
                    stock.setEps(updatedStock.getEps());
                    stock.setPer(updatedStock.getPer());
                    stock.setPbr(updatedStock.getPbr());
                    stock.setRoe(updatedStock.getRoe());
                    stock.setSector(updatedStock.getSector());
                    stock.setListedDate(updatedStock.getListedDate());
                    stock.setDescription(updatedStock.getDescription());
                    return stockInfoRepository.save(stock);
                });
    }


    @Transactional
    public void deleteStock(String stockCode) {
        StockInfo stockInfo = stockInfoRepository.findByStockCode(stockCode)
                .orElseThrow(() -> new EntityNotFoundException("Stock not found with code: " + stockCode));
        try {
            stockInfoRepository.delete(stockInfo);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete stock with code: " + stockCode, e);
        }
    }


    @Transactional(readOnly = true)
    public List<StockInfo> searchStocksByCompany(String companyName) {
        return stockInfoRepository.findByCompanyContaining(companyName);
    }
}