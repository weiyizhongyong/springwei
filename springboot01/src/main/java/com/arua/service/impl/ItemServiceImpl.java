package com.arua.service.impl;

import com.arua.dao.ItemDO;
import com.arua.dao.ItemStockDO;
import com.arua.dao.SequenceDO;
import com.arua.error.BusinessException;
import com.arua.error.EmBusinessError;
import com.arua.mapper.ItemDOMapper;
import com.arua.mapper.ItemStockDOMapper;
import com.arua.mapper.SequenceDOMapper;
import com.arua.service.ItemService;
import com.arua.service.model.ItemModel;
import com.arua.vaildator.VaildatinResult;
import com.arua.vaildator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.ref.PhantomReference;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private ItemDOMapper itemDOMapper;
    @Autowired
    private ItemStockDOMapper itemStockDOMapper;


    private ItemStockDO convertItemsStockDOFromModel(ItemModel itemModel){
        if(itemModel == null){
            return null;

        }
        ItemStockDO itemStockDO = new ItemStockDO();
        BeanUtils.copyProperties(itemModel,itemStockDO);
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;


    }

    private ItemDO comvertItemDOFromItemModel(ItemModel itemModel){
        if(itemModel == null){
            return null;

        }
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel,itemDO);
        itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;

    }

    @Override
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //校验入参
        VaildatinResult re = validator.validate(itemModel);
        if(re.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_UALIDATION_ERROR,re.getErrMsg());

        }
        //转化itemmodel --》dataobject
        ItemDO itemDO = this.comvertItemDOFromItemModel(itemModel);

        //写入数据库
        itemDOMapper.insert(itemDO);
        itemModel.setId(itemDO.getId());
        ItemStockDO itemStockDO = this.convertItemsStockDOFromModel(itemModel);
        itemStockDOMapper.insert(itemStockDO);
        //返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listTtem() {
        List<ItemDO> itemDOS = itemDOMapper.listItem();//List<ItemDO> listItem();
        List<ItemModel> collect = itemDOS.stream().map(
                itemDO -> {
                    ItemStockDO itemStockDO = itemStockDOMapper.selectByIteamId(itemDO.getId());
                    // ItemStockDO selectByIteamId(Integer id);
                    ItemModel itemModel = this.convertModelFromDataObject(itemDO, itemStockDO);
                    return itemModel;

                }
        ).collect(Collectors.toList());
        return collect;
    }




    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if(itemDO == null){
            return null;

        }
        ItemStockDO itemStockDO = itemStockDOMapper.selectByIteamId(itemDO.getId());
        //将dataobject -》Model
        ItemModel itemModel = convertModelFromDataObject(itemDO, itemStockDO);

        return itemModel;
    }

    @Override
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        int i = itemStockDOMapper.decreaseStock(itemId, amount);
        //int decreaseStock(@Param(value = "itemId") Integer itemId, @Param(value = "amount") Integer amount);
        if(i > 0){
            //数据跟新
            return true;
        }else
            //跟新失败
        return false;
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDOMapper.updateBySales(itemId,amount);
        //void upupdateBySales(@Param(value = "itemId") Integer itemId, @Param(value = "amount") Integer amount);

    }

    private ItemModel convertModelFromDataObject(ItemDO itemDO,ItemStockDO itemStockDO){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO,itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;

    }
}
