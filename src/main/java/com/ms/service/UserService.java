package com.ms.service;

import com.ms.error.BusinessException;
import com.ms.service.model.UserModel;

public interface UserService {

    // 通过用户ID获取用户信息
    UserModel getUserById(Integer id);

    // 注册接口
    void register(UserModel userModel) throws BusinessException;

    // 登录接口
    UserModel validateLogin(String telphone, String encodePassword) throws BusinessException;
}
