package com.arua.service;

import com.arua.error.BusinessException;
import com.arua.service.model.OrderModel;

import java.util.List;

public interface OrderService {
    OrderModel createOrder(Integer userId,Integer itemId,Integer amount) throws BusinessException;
    List<OrderModel> listOrder();

}
