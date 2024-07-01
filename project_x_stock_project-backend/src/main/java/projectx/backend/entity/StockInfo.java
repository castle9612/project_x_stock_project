package projectx.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 주식 정보를 나타내는 엔티티 클래스
 */
@Entity
@Table(name = "stock_info")
@Getter @Setter
public class StockInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "stock_code", unique = true, nullable = false)
    private String stockCode;  // 종목 코드

    @Column(name = "stock_list", nullable = false)
    private String stockList;  // 종목명

    @Column(name = "company", nullable = false)
    private String company;    // 회사명

    @Column(name = "stock_type")
    private String stockType;  // 주식 유형

    @Column(name = "market_capitalization", nullable = false)
    private BigInteger marketCapitalization;  // 시가총액

    @Column(name = "dividend", nullable = false)
    private BigDecimal dividend;   // 배당금

    @Column(name = "current_price", nullable = false)
    private BigDecimal currentPrice;  // 현재 가격

    @Column(name = "volume", nullable = false)
    private BigInteger volume;       // 거래량

    @Column(name = "eps", nullable = false)
    private BigDecimal eps;        // 주당순이익

    @Column(name = "per")
    private BigDecimal per;        // 주가수익비율

    @Column(name = "pbr")
    private BigDecimal pbr;        // 주가순자산비율

    @Column(name = "roe")
    private BigDecimal roe;        // 자기자본이익률

    @Column(name = "sector")
    private String sector;     // 업종

    @Column(name = "listed_date")
    private LocalDate listedDate;  // 상장일

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;  // 회사 설명

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;  // 마지막 업데이트 시간

    /**
     * 엔티티 저장 또는 업데이트 시 자동으로 lastUpdated 필드를 현재 시간으로 설정
     */
    @PrePersist
    @PreUpdate
    public void updateLastModifiedDate() {
        this.lastUpdated = LocalDateTime.now();
    }

    // 기본 생성자
    public StockInfo() {
    }

    /**
     * 모든 필드를 포함한 생성자
     */
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