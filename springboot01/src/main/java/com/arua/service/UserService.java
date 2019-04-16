package com.arua.service;

import com.arua.error.BusinessException;
import com.arua.service.model.UserModel;

public interface UserService {
    //通过用户ID获取用户对象的方法
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;

    /**
     *
     * @param telphone  用户注册手机
     * @param password  用户加密后的密码
     * @throws BusinessException
     */
    UserModel validateLogin(String telphone,String password) throws BusinessException;
}
