package com.arua.controller;

import com.alibaba.druid.util.StringUtils;
import com.arua.controller.viewobject.UserVO;
import com.arua.error.BusinessException;
import com.arua.error.EmBusinessError;
import com.arua.response.CommonReturnType;
import com.arua.service.UserService;
import com.arua.service.model.UserModel;

import org.apache.tomcat.util.security.MD5Encoder;
import org.codehaus.groovy.transform.SourceURIASTTransformation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    //用户登入接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST})//,consumes = {CONTENT_TYPE_FORMED}
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telphone")String telphone,
                                  @RequestParam(name = "password")String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, BusinessException {
        //入参校验
        //System.out.println(telphone +password);
        if(org.apache.commons.lang3.StringUtils.isEmpty(telphone) ||
                StringUtils.isEmpty(password)){
            //System.out.println("1111111111");
            throw new BusinessException(EmBusinessError.PARAMETER_UALIDATION_ERROR);

        }
        //用户登录服务，用来校验用户登入是否合法
        UserModel userModel = userService.validateLogin(telphone, this.EncodeByMd5(password));
        //System.out.println(userModel.toString());
        //将登入凭证加入到用户登入成功的session
        this.httpServletRequest.getSession().setAttribute("LOGIN",true);
        Boolean is_login = (Boolean)httpServletRequest.getSession().getAttribute("LOGIN");
        //System.out.println(is_login);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
        UserModel userModel2 = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");
        //System.out.println(userModel2);
        return CommonReturnType.create(userModel);

    }
    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST})//,consumes = {CONTENT_TYPE_FORMED}
    @ResponseBody
    public CommonReturnType register(
            @RequestParam(name = "telphone")String telphone,
            @RequestParam(name = "otpCode")String otpCode,
            @RequestParam(name = "name")String name,
            @RequestParam(name = "gender")Integer gender,
            @RequestParam(name = "age")Integer age,
            @RequestParam(name = "password")String password
    ) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //System.out.println(telphone + " "+otpCode +" " + name + " "+gender + " "+ age +" "+password);
        //验证手机号和对应的otpCode相符合
        String  inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telphone);
        //System.out.println(inSessionOtpCode);
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,  inSessionOtpCode))
            throw new BusinessException(EmBusinessError.PARAMETER_UALIDATION_ERROR, "短信验证错误");

        //用户的注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        userModel.setThridPartyId("fweq");
        userModel.setPassword(this.EncodeByMd5(password));
        userService.register(userModel);

        return CommonReturnType.create(null);

    }
    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定一个计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密字符串
        String encode = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return encode;

    }

    //用户获取otp短信接口
    @RequestMapping(value = "getOtp",method = {RequestMethod.POST})//,consumes = {CONTENT_TYPE_FORMED}
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telphone")String telphone){
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int i = random.nextInt(8999);
        i = i +1000;
        String s = String.valueOf(i);

        //将OTP验证码同时对应的用户的手机号关联 使用httpsession的方式绑定，用redis
        httpServletRequest.getSession().setAttribute(telphone,s);


        //将OTP验证码通过短信通道发送给用户 先省略 在生成不能用
        System.out.println("telphone =" + telphone +" & optCode =" +s);

        return CommonReturnType.create(null);

    }
    @RequestMapping("getIndex")
    public String getIndex(){
        return "thymeleaf/getOtp";
    }
    @RequestMapping("getLogin")
    public String getLogin(){
        return "thymeleaf/login";
    }
    @RequestMapping("getRegister")
    public String getRegister(){
        return "thymeleaf/register";
    }
    @RequestMapping("/getUser")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id")Integer id) throws BusinessException {
        //调用service服务器对应id的用户对象并返回给前端
        UserModel userById = userService.getUserById(id);

        //若获取的对应用户信息不存在
        if(userById == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);

        }
        //将核心领域模型用户对象转换为可供ui使用的viewobject
        UserVO userVO = conventFromModel(userById);
        return CommonReturnType.create(userVO);

    }
    @RequestMapping("/getUser1")
    @ResponseBody
    public  UserModel getUser1(@RequestParam(name="id")Integer id){
        //调用
        UserModel userById = userService.getUserById(id);
        return userById;

    }
    private UserVO conventFromModel(UserModel userModel){
        if(userModel == null){
            return null;

        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;

    }
    @RequestMapping("center")
    public String center() {
        return "thymeleaf/index";//"thymeleaf/center/center";
    }
    @RequestMapping("demo")
    public String demo() {
        return "thymeleaf/demo";//"thymeleaf/center/center";
    }
    @RequestMapping("/index")
    public String index(ModelMap map) {
        map.addAttribute("name", "thymeleaf-imooc");
        return "thymeleaf/index";
    }

}
