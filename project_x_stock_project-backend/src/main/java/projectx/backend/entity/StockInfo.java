package projectx.backend.entity;

import jakarta.persistence.*;
import lombok.Setter;

@Table(name = "stockInfo")
@Entity
@Setter

public class StockInfo {
    //종목코드, 종목명, 기업명, 주식종류, 시가 총액, 배당금, ...
    @Id
    @Column(name = "stock_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stockCode; //종목코드
    private String stocklist; //종목명
    private String company; //기업명
    private String stockType; //주식종류
    private Integer marketCapitalization; //시가총액
    private Integer Dibidend; // 배당금
    
}
