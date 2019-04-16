package com.arua.service.model;

import java.util.List;

public class ListOrderModel {
    private List<OrderModel> orderModelList;

    public List<OrderModel> getOrderModelList() {
        return orderModelList;
    }

    public void setOrderModelList(List<OrderModel> orderModelList) {
        this.orderModelList = orderModelList;
    }
    public ListOrderModel(){}
    public ListOrderModel(List<OrderModel> orderModelList){
        super();
        this.orderModelList = orderModelList;
    }
}
