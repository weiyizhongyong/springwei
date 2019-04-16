package com.arua.controller;

import com.arua.controller.viewobject.ItemVO;
import com.arua.error.BusinessException;
import com.arua.error.EmBusinessError;
import com.arua.response.CommonReturnType;
import com.arua.service.OrderService;
import com.arua.service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.naming.ldap.PagedResultsControl;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    //封装下单请求
    @RequestMapping(value = "/createorder",method = {RequestMethod.POST})//,consumes = {CONTENT_TYPE_FORMED}
    @ResponseBody
    public CommonReturnType createOrder(
            @RequestParam(name = "itemId")Integer itemId,
            @RequestParam(name = "amount")Integer amount
            ) throws BusinessException {
        //System.out.println(itemId +"  "+ amount);
        Boolean is_login = (Boolean)httpServletRequest.getSession().getAttribute("LOGIN");
        //System.out.println(is_login);
        if(is_login == null || !is_login.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登入");
        }
        //获取用户信息
        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");

        //System.out.println(userModel.toString());


        OrderModel order = orderService.createOrder(userModel.getId(), itemId, amount);
        //System.out.println(order.toString());


        return CommonReturnType.create(order);
    }
    @RequestMapping(value = "/jsonOrder",method = {RequestMethod.POST})//,consumes = {CONTENT_TYPE_FORMED}
    @ResponseBody
    public CommonReturnType jsonOrder(
            @RequestBody  List<JsonModel> orderModel
    ) throws BusinessException {

        for (JsonModel ss:orderModel){
            //System.out.println(ss.toString());
        }


        Boolean is_login = (Boolean)httpServletRequest.getSession().getAttribute("LOGIN");
        //System.out.println(is_login);
        if(is_login == null || !is_login.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登入");
        }
        //获取用户信息
        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");

        //System.out.println(userModel.toString());
        for(JsonModel jj: orderModel){
            OrderModel order = orderService.createOrder(userModel.getId(), jj.getId(), jj.getAmount());
        }

        //OrderModel order = orderService.createOrder(userModel.getId(), itemId, amount);
//        System.out.println(order.toString());


        return CommonReturnType.create(null);
    }
    @RequestMapping(value = "/listOrder",method = {RequestMethod.GET})//,consumes = {CONTENT_TYPE_FORMED}
    @ResponseBody
    public CommonReturnType listOrder(){
        List<OrderModel> orderModels = orderService.listOrder();
        /*List<ItemModel> itemModels = itemService.listTtem();
        List<ItemVO> collect = itemModels.stream().map(itemModel -> {
            ItemVO itemVO = this.convertVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(collect);*/
        return CommonReturnType.create(orderModels);
    }
    @RequestMapping("/getlist")
    public String getlist(){
        return "thymeleaf/getOrderList";
    }

}
