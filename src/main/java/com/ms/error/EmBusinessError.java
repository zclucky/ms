package com.ms.error;

public enum EmBusinessError implements CommomError {

    //10000开头为通用错误信息
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),
    UNKNOWN_ERROR(10002, "未知错误"),

    //20000开头为用户相关错误信息
    USER_NOT_EXIST(20001, "用户不存在"),
    LOGIN_ERROR(20002, "用户名或密码错误"),

    ;

    private int errorCode;
    private String errorMsg;

    private EmBusinessError(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    @Override
    public CommomError setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}
