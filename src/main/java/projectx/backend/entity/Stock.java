package projectx.backend.entity;

import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "stock")
@Entity
@Setter
public class Stock {

    // 날짜, 상한가, 하한가, 누적거래량

    @Id
    @Column(name = "stock_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockCode;
    private LocalDate date;
    private String maxPrice;
    private String minPrice;
    private String accumTrans;

}
