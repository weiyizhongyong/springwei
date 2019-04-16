package com.arua.service.model;

public class JsonModel {
    private Integer id;

    private Integer amount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "JsonModel{" +
                "id=" + id +
                ", amount=" + amount +
                '}';
    }
}
