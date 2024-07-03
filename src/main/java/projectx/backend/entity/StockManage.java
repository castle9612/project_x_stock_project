package projectx.backend.entity;

import jakarta.persistence.*;

@Entity
public class StockManage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private StockInfo stockInfo;

    @Column(nullable = false)
    private double money; // 금액

    @Column(nullable = false)
    private double earningsRate; // 수익률

    @Column(nullable = false)
    private int quantity; // 수량

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StockInfo getStockInfo() {
        return stockInfo;
    }

    public void setStockInfo(StockInfo stockInfo) {
        this.stockInfo = stockInfo;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getEarningsRate() {
        return earningsRate;
    }

    public void setEarningsRate(double earningsRate) {
        this.earningsRate = earningsRate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}