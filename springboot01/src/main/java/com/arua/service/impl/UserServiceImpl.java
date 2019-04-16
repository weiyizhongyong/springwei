package com.arua.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.arua.dao.UserDO;

import com.arua.dao.UserPasswordDO;
import com.arua.error.BusinessException;
import com.arua.error.EmBusinessError;
import com.arua.mapper.UserDOMapper;
import com.arua.mapper.UserPasswordDOMapper;
import com.arua.service.UserService;
import com.arua.service.model.UserModel;
import com.arua.vaildator.VaildatinResult;
import com.arua.vaildator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswrodDOMapper;
    @Autowired
    private ValidatorImpl validator;
    @Override
    public UserModel getUserById(Integer id) {
        //
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO == null){
            return null;
        }
        UserPasswordDO userPasswrodDO = userPasswrodDOMapper.selectByUserId(userDO.getId());
       return convertFromDataObject(userDO,userPasswrodDO);

    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_UALIDATION_ERROR);
        }
        /*if(StringUtils.isEmpty(userModel.getName()) ||
                userModel.getGender() ==null ||
                userModel.getAge() == null ||
                StringUtils.isEmpty(userModel.getTelphone())){
            throw new BusinessException(EmBusinessError.PARAMETER_UALIDATION_ERROR);
        }*/
        VaildatinResult validate = validator.validate(userModel);
        if(validate.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_UALIDATION_ERROR,validate.getErrMsg());


        }

        //实现model ——》dataobject 方法
        UserDO userDO = convertFromModel(userModel);
        userDOMapper.insert(userDO);
        userModel.setId(userDO.getId());
        //UserPasswordDO userPasswrodDO = convertPasswordFromModel(userModel);
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswrodDOMapper.insert(userPasswordDO);
        return;


    }

    @Override
    public UserModel validateLogin(String telphone, String password) throws BusinessException {
        //通过用户的手机获取用户信息
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        if (userDO == null) {
        throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswrodDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO, userPasswordDO);

        //比对用户信息内加密的密码是否和传输进来的密码相匹配
        if(!StringUtils.equals(password,userModel.getPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;

    }

    public UserPasswordDO convertPasswordFromModel(UserModel userModel){
        /*if(userModel != null){
            return  null;

        }*/
        // userPasswrodDO = new UserPasswrodDO();
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        //System.out.println(userModel.getPassword());
        userPasswordDO.setPassword(userModel.getPassword());
        //System.out.println(userModel.getId());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;


    }

    public UserDO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;

        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;

    }
    public UserModel convertFromDataObject(UserDO suerDO, UserPasswordDO userDOMapper){
        if(suerDO ==null){
            return  null;

        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(suerDO,userModel);
        if(userDOMapper != null){
            userModel.setPassword(userDOMapper.getPassword());
        }

        return  userModel;

    }
}
