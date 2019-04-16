package com.arua;

import com.arua.controller.ItemController;
import com.arua.controller.UserController;
import com.arua.dao.UserDO;
import com.arua.dao.UserPasswordDO;
import com.arua.error.BusinessException;
import com.arua.mapper.ItemDOMapper;
import com.arua.mapper.ItemStockDOMapper;
import com.arua.mapper.UserDOMapper;
import com.arua.mapper.UserPasswordDOMapper;
import com.arua.service.ItemService;
import com.arua.service.OrderService;
import com.arua.service.UserService;
import com.arua.service.model.UserModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Tests {
	@Autowired
	private UserService userService;
	@Autowired
	UserController userController;
	@Autowired
	private UserDOMapper userDOMapper;
	@Autowired
	private UserPasswordDOMapper userPasswrodDOMapper;
	@Autowired
	private ItemDOMapper itemDOMapper;
	@Autowired
	private ItemService itemService;
	@Autowired
	private ItemController itemController;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ItemStockDOMapper itemStockDOMapper;





	/*@Test
	public void contextLoads() throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
		UserModel userById = userService.getUserById(1);
		UserModel userModel = new UserModel();
		userModel.setName("123");
		Integer gender = 2;
		userModel.setGender(new Byte(String.valueOf(gender.intValue())));
		userModel.setAge(12);
		userModel.setTelphone("ewwer");
		userModel.setRegisterMode("byphone");
		userModel.setPassword("fwewe");
		userModel.setThridPartyId("fweq");
		userService.register(userModel);
		UserDO userDO = new UserDO();
		userDO.setName("fe");
		userDO.setAge(20);
		Integer gender = 2;
		userDO.setGender(new Byte(String.valueOf(gender.intValue())));
		userDO.setRegisterMode("fweew");
		userDO.setTelphone("fwe");
		userDO.setThridPartyId("qe");
		userDO.getRegisterMode();
		userDOMapper.insertSelect(userDO);
		UserPasswrodDO uu = new UserPasswrodDO();
		uu.setUserId(3);
		uu.setPassword("w");
		userPasswrodDOMapper.insert(uu);
		ItemDO itemDO = new ItemDO();
		itemDO.setTitle("iphone6");
		itemDO.setPrice(6666D);
		itemDO.setDescription("非常好用");
		itemDO.setSales(0);
		itemDO.setImgUrl("https://img10.360buyimg.com/n7/jfs/t1/3405/18/3537/69901/5b997c0aE5dc8ed9f/a2c208410ae84d1f.jpg");
		itemDOMapper.insert(itemDO);

		ItemModel itemModel = new ItemModel();
		itemModel.setTitle("NeI");
		itemModel.setPrice(new BigDecimal(25));
		itemModel.setStock(52);
		itemModel.setDescription("wfewew");
		itemModel.setSales(0);
		itemModel.setImgUrl("wwwd");
		ItemModel itemById = itemService.createItem(itemModel);
		System.out.println(itemById.getId()+itemById.getStock());
		ItemVO itemVO = itemController.convertVOFromModel(itemById);
		System.out.println(itemVO.getDescription());

		OrderModel order = orderService.createOrder(1, 4, 1);
		System.out.println(order.toString());
		int i = itemStockDOMapper.decreaseStock(1, 1);
	    ItemStockDO itemStockDO = itemStockDOMapper.selectByIteamId(1);
		System.out.println(itemStockDO.toString());
		ItemModel itemModel = new ItemModel();
		itemModel.setTitle("大数据");
		itemModel.setPrice(new BigDecimal(450));
		itemModel.setStock(99);
		itemModel.setDescription("海量的数据");
		itemModel.setSales(0);
		itemModel.setImgUrl("www.xxx.com");
		ItemModel item = itemService.createItem(itemModel);
		List<ItemDO> itemDOS = itemDOMapper.listItem();
		for(ItemDO l:itemDOS){
			System.out.println(l.toString());

		}
		//int i = itemStockDOMapper.decreaseStock(4, 2);
		UserModel userModel = userService.validateLogin("123", this.EncodeByMd5("123"));
		System.out.println(userModel.toString());



	}*/
    @Test
	public  void test01(){
		UserDO userDO = userDOMapper.selectByTelphone("123");
		System.out.println(userDO.toString());

		UserPasswordDO userPasswordDO = userPasswrodDOMapper.selectByUserId(9);
		UserPasswordDO userPasswordDO1 = userPasswrodDOMapper.selectByPrimaryKey(4);
		System.out.println(userPasswordDO1.toString());

		System.out.println(userPasswordDO.toString());
	}
	public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		//确定一个计算方法
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		BASE64Encoder base64Encoder = new BASE64Encoder();
		//加密字符串
		String encode = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
		return encode;

	}
}
