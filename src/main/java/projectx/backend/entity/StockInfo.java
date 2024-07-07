package projectx.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private int marketCapitalization;

    @Column(name = "dividend", nullable = false)
    private double dividend;

    @Column(name = "current_price", nullable = false)
    private double currentPrice;

    @Column(name = "volume", nullable = false)
    private int volume;

    @Column(name = "eps", nullable = false)
    private double eps;

    @Column(name = "per")
    private double per;

    @Column(name = "pbr")
    private double pbr;

    @Column(name = "roe")
    private double roe;

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

    public StockInfo(Long id, String stockCode, String stockList, String company, String stockType, int marketCapitalization, double dividend, double currentPrice, int volume, double eps, double per, double pbr, double roe, String sector, LocalDate listedDate, String description, LocalDateTime lastUpdated) {
        this.id = id;
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
        this.lastUpdated = lastUpdated;
    }
}