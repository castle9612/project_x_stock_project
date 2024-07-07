package projectx.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class StockGraphDto {

    // { date: new Date(2020, 1, 1), open: 100, high: 105, low: 95, close: 102 }

    private LocalDate date;
    private int open;
    private int high;
    private int low;
    private int close;
}
