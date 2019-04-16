package com.arua.service.impl;

import com.arua.dao.ItemDO;
import com.arua.dao.ItemStockDO;
import com.arua.dao.OrderDO;
import com.arua.dao.SequenceDO;
import com.arua.error.BusinessException;
import com.arua.error.EmBusinessError;
import com.arua.mapper.OrderDOMapper;
import com.arua.mapper.SequenceDOMapper;
import com.arua.service.ItemService;
import com.arua.service.OrderService;
import com.arua.service.UserService;
import com.arua.service.model.ItemModel;
import com.arua.service.model.OrderModel;
import com.arua.service.model.UserModel;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDOMapper orderDOMapper;
    @Autowired
    private SequenceDOMapper sequenceDOMapper;
    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException {
        //1.校验下单状态，下单的商品是否存在，用户是否个发，购买数量是否正确
        ItemModel itemById = itemService.getItemById(itemId);
        //System.out.println(itemById.toString());

        if(itemById == null){
            throw new BusinessException(EmBusinessError.PARAMETER_UALIDATION_ERROR,"商品信息不存在");

        }
        UserModel userById = userService.getUserById(userId);
        //System.out.println(userById.toString());
        if(userById == null){
            throw new BusinessException(EmBusinessError.PARAMETER_UALIDATION_ERROR,"用户不存在");
        }
        if(amount < 0 || amount >99){
            throw new BusinessException(EmBusinessError.PARAMETER_UALIDATION_ERROR,"数量不合理");
        }
        //2.落单减库存，支付减库存
        boolean result = itemService.decreaseStock(itemId, amount);
        if(!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_EXIST);
        }

        //3.订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        orderModel.setItemPrice(itemById.getPrice());
        orderModel.setOrderPrice(itemById.getPrice().multiply(new BigDecimal(amount)));
        //生成订单号
        orderModel.setId(geterateOrderNo());
        OrderDO orderDO = convertFromOrderModel(orderModel);
        orderDOMapper.insert(orderDO);

        //加上商品的销量
        itemService.increaseSales(itemId,amount);

        //返回前端

        return orderModel;
    }

    @Override
    public List<OrderModel> listOrder() {
        List<OrderDO> orderDOS = orderDOMapper.selectAll();
        List<OrderModel> collect = orderDOS.stream().map(
                mOrderDOS -> {


                    OrderModel orderModel = this.convertModelFromDataObject(mOrderDOS);
                    return orderModel;

                }
        ).collect(Collectors.toList());
        return collect;
    }

    private OrderModel convertModelFromDataObject(OrderDO mOrderDOS) {
        if(mOrderDOS == null){
            return null;

        }
        OrderModel orderModel = new OrderModel();
        BeanUtils.copyProperties(mOrderDOS,orderModel);
        orderModel.setItemPrice(new BigDecimal(mOrderDOS.getItemPrice()));
        orderModel.setOrderPrice(new BigDecimal(mOrderDOS.getOrderPrice()));

        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)//开启新的事物
    public String geterateOrderNo(){
        //订单号有16位
        StringBuilder stringBuilder = new StringBuilder();

        //前八位为时间信息
        //Data data = new Data();
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);

        //中间6位为自增序列
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        //SequenceDO getSequenceByName(String s);
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue()+sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKey(sequenceDO);
        //void updateByPrimaryKey(SequenceDO sequenceDO);
        String sequenceStr = String.valueOf(sequence);
        for(int i =0;i< 6 - sequenceStr.length();i++){
            stringBuilder.append("0");

        }
        stringBuilder.append(sequenceStr);


        //最后2位为分库分表位
        stringBuilder.append("00");
        return  stringBuilder.toString();
    }
    private OrderDO convertFromOrderModel(OrderModel orderModel){
        if(orderModel == null){
            return  null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel,orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;

    }
}
