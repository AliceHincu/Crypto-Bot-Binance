package com.example.cryptobot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "timeunix")
    private BigInteger timeunix;
    @Column(name = "type")
    private String type;
    @Column(name = "beforecurrency")
    private String beforecurrency;
    @Column(name = "beforequantity")
    private double beforequantity;
    @Column(name = "aftercurrency")
    private String aftercurrency;
    @Column(name = "afterquantity")
    private double afterquantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Transaction(int id, BigInteger timeunix, String type, String beforecurrency, double beforequantity, String aftercurrency, double afterquantity) {
        this.id = id;
//        this.timeunix = LocalDateTime.ofEpochSecond(Long.parseLong(timeunix.toString()), 0, ZoneOffset.UTC);
        this.timeunix = timeunix;
        this.type = type;
        this.beforecurrency = beforecurrency;
        this.beforequantity = beforequantity;
        this.aftercurrency = aftercurrency;
        this.afterquantity = afterquantity;
    }

    public Transaction() {

    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", timeunix=" + timeunix +
                ", type='" + type + '\'' +
                ", beforecurrency='" + beforecurrency + '\'' +
                ", beforequantity=" + beforequantity +
                ", aftercurrency='" + aftercurrency + '\'' +
                ", afterquantity=" + afterquantity +
                '}';
    }

    public BigInteger getTimeunix() {
        return timeunix;
    }

    public void setTimeunix(BigInteger timeunix) {
        this.timeunix = timeunix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBeforecurrency() {
        return beforecurrency;
    }

    public void setBeforecurrency(String beforecurrency) {
        this.beforecurrency = beforecurrency;
    }

    public double getBeforequantity() {
        return beforequantity;
    }

    public void setBeforequantity(double beforequantity) {
        this.beforequantity = beforequantity;
    }

    public String getAftercurrency() {
        return aftercurrency;
    }

    public void setAftercurrency(String aftercurrency) {
        this.aftercurrency = aftercurrency;
    }

    public double getAfterquantity() {
        return afterquantity;
    }

    public void setAfterquantity(double afterquantity) {
        this.afterquantity = afterquantity;
    }
}
