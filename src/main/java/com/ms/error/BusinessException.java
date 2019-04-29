package com.ms.error;

public class BusinessException extends Exception implements CommomError {

    private CommomError commomError;

    public BusinessException(CommomError commomError){
        super();
        this.commomError = commomError;
    }

    public BusinessException(CommomError commomError, String errorMsg){
        super();
        this.commomError = commomError;
        this.commomError.setErrorMsg(errorMsg);
    }


    @Override
    public int getErrorCode() {
        return this.commomError.getErrorCode();
    }

    @Override
    public String getErrorMsg() {
        return this.commomError.getErrorMsg();
    }

    @Override
    public CommomError setErrorMsg(String errorMsg) {
        this.commomError.setErrorMsg(errorMsg);
        return this.commomError;
    }
}
