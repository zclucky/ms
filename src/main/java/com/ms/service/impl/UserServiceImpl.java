package com.ms.service.impl;

import com.ms.dao.UserDOMapper;
import com.ms.dao.UserPasswordDOMapper;
import com.ms.dataobject.UserDO;
import com.ms.dataobject.UserPasswordDO;
import com.ms.error.BusinessException;
import com.ms.error.EmBusinessError;
import com.ms.service.UserService;
import com.ms.service.model.UserModel;
import com.ms.validator.ValidatorImp;
import com.ms.validator.ValidatorResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    private ValidatorImp validator;

    @Override
    public UserModel getUserById(Integer id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if (userDO == null){
            return null;
        }
        return convertFromDataObject(userDO,userPasswordDOMapper.selectByUserId(id) );
    }

    @Override
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
//        if(StringUtils.isEmpty(userModel.getTelphone())
//                ||StringUtils.isEmpty(userModel.getName())
//                ||userModel.getAge() == null
//                ||userModel.getGendor() == null
//        ){
//        }
        //验证用户输入信息
        ValidatorResult result = validator.validate(userModel);
        if (result.getHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        // 插入用户信息
        UserDO userDO = convertFromModel(userModel);
        try{
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已重复注册");
        }
        userModel.setId(userDO.getId());

        // 插入密码信息
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }

    @Override
    public UserModel validateLogin(String telphone, String encodePassword) throws BusinessException {
        // 通过手机号码获取用户信息
        UserDO userDO = userDOMapper.selectByPhone(telphone);
        if(userDO == null){
            throw new BusinessException(EmBusinessError.LOGIN_ERROR);
        }

        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);

        // 判断传入的密码和用户信息的密码是否一致
        if (!StringUtils.equals(userModel.getEncrptPassword(),encodePassword)){
            throw new BusinessException(EmBusinessError.LOGIN_ERROR);
        }
        return userModel;
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setUserId(userModel.getId());
        BeanUtils.copyProperties(userModel,userPasswordDO);
        return userPasswordDO;
    }

    private UserDO convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if (userDO == null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);
        if (userPasswordDO != null){
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userModel;
    }
}
