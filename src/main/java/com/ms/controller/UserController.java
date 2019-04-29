package com.ms.controller;

import com.alibaba.druid.util.StringUtils;
import com.ms.controller.viewobject.UserVO;
import com.ms.error.BusinessException;
import com.ms.error.EmBusinessError;
import com.ms.response.CommomRetrunType;
import com.ms.service.UserService;
import com.ms.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller("user")
@RequestMapping("user")
@CrossOrigin(allowCredentials="true",allowedHeaders="*")
public class UserController extends BaseController {

    @Autowired
    private  UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping("get")
    @ResponseBody
    public CommomRetrunType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        // 调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);
        if (userModel == null){
            throw  new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        //将核心领域模型转换为可供UI使用的viewobject
        UserVO userVO = convertFromModel(userModel);
        return CommomRetrunType.create(userVO);
    }

    // 用户注册接口
    @RequestMapping(value = "register", method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommomRetrunType register(@RequestParam(name = "telphone") String telphone,
                                     @RequestParam(name = "optCode") String optCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "gendor") Integer gendor,
                                     @RequestParam(name = "password") String password
                                     ) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //判断opt验证码是否和session中的code一致
        String sessionOptCode = (String) httpServletRequest.getSession().getAttribute(telphone);
        System.out.println(sessionOptCode);
        if(!StringUtils.equals(sessionOptCode,optCode)){
            throw new  BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码错误");
        }

        //注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setAge(age);
        userModel.setGendor(new Byte(String.valueOf(gendor.intValue())));
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("1");
        userModel.setEncrptPassword(encodeByMd5(password));
        userService.register(userModel);
        return CommomRetrunType.create(null);
    }

    //用户登录
    @RequestMapping(value = "login", method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommomRetrunType login(@RequestParam(name = "telphone") String telphone,@RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        // 入参校验
        if(StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        // 用户登录服务，用来判断用户登录是否合法
        UserModel userModel = userService.validateLogin(telphone,encodeByMd5(password));

        // 将用户凭证加入到session中去
        httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        return CommomRetrunType.create(null);
    }

    // 用户获取opt短信的接口
    @RequestMapping(value = "getopt", method = {RequestMethod.POST},consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommomRetrunType getOpt(@RequestParam(name = "telphone") String telphone){
        // 需要按一定规则生成OPT验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String optCode = String.valueOf(randomInt);


        // 将OPT验证码同时与对应的手机号关联
        httpServletRequest.getSession().setAttribute(telphone,optCode);

        // 将OPT验证码通过短信的通道发送给用户
        Map<String,String> codeMap = new HashMap<>();
        codeMap.put("telphone",telphone);
        codeMap.put("optCode",optCode);
        System.out.println("手机号码是：" + telphone + ", 验证码是： " + optCode);
        return CommomRetrunType.create(codeMap,"success");
    }





    private UserVO convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }

    private String encodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return  base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
    }





}
