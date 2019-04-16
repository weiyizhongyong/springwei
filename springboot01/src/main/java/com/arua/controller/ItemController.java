package com.arua.controller;


import com.arua.controller.viewobject.ItemVO;
import com.arua.dao.Student;
import com.arua.error.BusinessException;
import com.arua.response.CommonReturnType;
import com.arua.service.ItemService;
import com.arua.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller("/item")
@RequestMapping("/item")
@CrossOrigin(origins = {"*"},allowCredentials = "true")//跨域前后端分析
public class ItemController extends BaseController {
    @Autowired
    private ItemService itemService;
    //创建商品的controller
//    @RequestMapping(value = "/crateItem",method = {RequestMethod.POST})//,consumes = {CONTENT_TYPE_FORMED}
//    @ResponseBody
//    public CommonReturnType crateItem(
//            @RequestParam(name = "title")String title,
//            @RequestParam(name = "description")String description,
//            @RequestParam(name = "price")BigDecimal price,
//            @RequestParam(name = "stock")Integer stock,
//            @RequestParam(name = "imgUrl")String imgUrl
//    ) throws BusinessException {
//
//        //封装service 请求来创建商品
//        System.out.println(imgUrl);
//        ItemModel itemModel = new ItemModel();
//        itemModel.setTitle(title);
//        itemModel.setPrice(price);
//        itemModel.setStock(stock);
//        itemModel.setDescription(description);
//        itemModel.setSales(0);
//        itemModel.setImgUrl(imgUrl);
//        System.out.println(itemModel.toString());
//        ItemModel item = itemService.createItem(itemModel);
//
//        ItemVO itemVO = convertVOFromModel(item );
//        return CommonReturnType.create(itemVO);
//
//
//    }
    @RequestMapping(value = "/crateItem",method = {RequestMethod.POST})//consumes = {CONTENT_TYPE_FORMED}
    @ResponseBody
    public CommonReturnType crateItem(@RequestBody ItemModel itemModel) throws BusinessException {

        //封装service 请求来创建商品
//        ItemModel itemModel = new ItemModel();
//        itemModel.setTitle(title);
//        itemModel.setPrice(price);
//        itemModel.setStock(stock);
//        itemModel.setDescription(description);

       /* System.out.println(itemModel.toString());*/
//        itemModel.setImgUrl(imgUrl);
        ItemModel item = itemService.createItem(itemModel);

        ItemVO itemVO = convertVOFromModel(item );
        return CommonReturnType.create(itemVO);


    }
    @RequestMapping(value = "/student",method = {RequestMethod.POST})//,consumes = {CONTENT_TYPE_FORMED}
    @ResponseBody
    public CommonReturnType student(@RequestBody Student student){
        //System.out.println(student.toString());
        return null;
    }
    public   ItemVO convertVOFromModel(ItemModel itemModel){
        if(itemModel == null){
            return null;

        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        return itemVO;

    }

    //商品详情页浏览
    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id")Integer id){
        ItemModel itemById = itemService.getItemById(id);
        ItemVO itemVO = convertVOFromModel(itemById);
        return CommonReturnType.create(itemVO);
        //return "thymeleaf/getitem";

    }
    //商品列表页面浏览
    @RequestMapping(value = "/list",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem(){
        List<ItemModel> itemModels = itemService.listTtem();
        List<ItemVO> collect = itemModels.stream().map(itemModel -> {
            ItemVO itemVO = this.convertVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(collect);
    }
    @RequestMapping(value = "/list01",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType list01(){
        List<ItemModel> itemModels = itemService.listTtem();
        List<ItemVO> collect = itemModels.stream().map(itemModel -> {
            ItemVO itemVO = this.convertVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(collect);
    }
    @RequestMapping(value = "/getlist")
    public String getlist(){
        return "thymeleaf/listitem";
      //thymeleaf/listitem
    }
    @RequestMapping(value = "/listall")
    public String list(){
        return "thymeleaf/list";
        //thymeleaf/listitem
    }
    @RequestMapping(value = "/login01")
    public String login01(){
        return "thymeleaf/login01";
        //thymeleaf/listitem
    }
    @RequestMapping(value = "/page")
    public String page(){
        return "thymeleaf/page";
        //thymeleaf/listitem
    }
    @RequestMapping(value = "/shopcar")
    public String shopcar(){
        return "thymeleaf/shopcar";
        //thymeleaf/listitem
    }

    @RequestMapping(value = "/create")
    public String create(){
        return "thymeleaf/createitem";
    }

    @RequestMapping(value = "/getitem")
    public String getitem(Model model, @RequestParam(name = "id") Integer id) {
        ItemModel itemById = itemService.getItemById(id);
        ItemVO itemVO = convertVOFromModel(itemById);
        model.addAttribute("itemVO", itemVO);
        return "thymeleaf/getitem";
    }



}
