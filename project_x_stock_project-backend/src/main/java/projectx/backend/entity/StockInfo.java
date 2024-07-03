package projectx.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_info")
@Getter @Setter
public class StockInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "stock_code", unique = true, nullable = false)
    private String stockCode;  

    @Column(name = "stock_list", nullable = false)
    private String stockList;  

    @Column(name = "company", nullable = false)
    private String company;    

    @Column(name = "stock_type")
    private String stockType;

    @Column(name = "market_capitalization", nullable = false)
    private BigInteger marketCapitalization;  

    @Column(name = "dividend", nullable = false)
    private BigDecimal dividend;   

    @Column(name = "current_price", nullable = false)
    private BigDecimal currentPrice;  

    @Column(name = "volume", nullable = false)
    private BigInteger volume;      

    @Column(name = "eps", nullable = false)
    private BigDecimal eps;     

    @Column(name = "per")
    private BigDecimal per; 

    @Column(name = "pbr")
    private BigDecimal pbr;      

    @Column(name = "roe")
    private BigDecimal roe;        

    @Column(name = "sector")
    private String sector;   

    @Column(name = "listed_date")
    private LocalDate listedDate; 

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;  

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;  


    @PrePersist
    @PreUpdate
    public void updateLastModifiedDate() {
        this.lastUpdated = LocalDateTime.now();
    }


    public StockInfo() {
    }


    public StockInfo(String stockCode, String stockList, String company, String stockType,
                     BigInteger marketCapitalization, BigDecimal dividend, BigDecimal currentPrice, BigInteger volume,
                     BigDecimal eps, BigDecimal per, BigDecimal pbr, BigDecimal roe, String sector,
                     LocalDate listedDate, String description) {
        this.stockCode = stockCode;
        this.stockList = stockList;
        this.company = company;
        this.stockType = stockType;
        this.marketCapitalization = marketCapitalization;
        this.dividend = dividend;
        this.currentPrice = currentPrice;
        this.volume = volume;
        this.eps = eps;
        this.per = per;
        this.pbr = pbr;
        this.roe = roe;
        this.sector = sector;
        this.listedDate = listedDate;
        this.description = description;
    }
}