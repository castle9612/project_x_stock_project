package projectx.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "stock_list")
    private String stockList;  // 종목명

    @Column(name = "company")
    private String company;    // 회사명

    @Column(name = "stock_type")
    private String stockType;  // 주식 유형

    @Column(name = "market_capitalization")
    private Long marketCapitalization;  // 시가총액

    @Column(name = "dividend")
    private Double dividend;   // 배당금

    @Column(name = "current_price")
    private Double currentPrice;  // 현재 가격

    @Column(name = "volume")
    private Long volume;       // 거래량

    @Column(name = "eps")
    private Double eps;        // 주당순이익

    @Column(name = "per")
    private Double per;        // 주가수익비율

    @Column(name = "pbr")
    private Double pbr;        // 주가순자산비율

    @Column(name = "roe")
    private Double roe;        // 자기자본이익률

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
                     Long marketCapitalization, Double dividend, Double currentPrice, Long volume,
                     Double eps, Double per, Double pbr, Double roe, String sector,
                     LocalDate listedDate, String description) {
        // 필드 초기화 코드...
    }
}